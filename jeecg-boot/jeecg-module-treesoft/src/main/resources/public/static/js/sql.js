// CodeMirror, copyright (c) by Marijn Haverbeke and others
// Distributed under an MIT license: http://codemirror.net/LICENSE

(function(mod) {
  if (typeof exports == "object" && typeof module == "object") // CommonJS
    mod(require("codemirror"));
  else if (typeof define == "function" && define.amd) // AMD
    define(["codemirror"], mod);
  else // Plain browser env
    mod(CodeMirror);
})(function(CodeMirror) {
"use strict";

CodeMirror.defineMode("sql", function(config, parserConfig) {
  "use strict";

  var client         = parserConfig.client || {},
      atoms          = parserConfig.atoms || {"false": true, "true": true, "null": true},
      builtin        = parserConfig.builtin || {},
      keywords       = parserConfig.keywords || {},
      operatorChars  = parserConfig.operatorChars || /^[*+\-%<>!=&|~^]/,
      support        = parserConfig.support || {},
      hooks          = parserConfig.hooks || {},
      dateSQL        = parserConfig.dateSQL || {"date" : true, "time" : true, "timestamp" : true};

  function tokenBase(stream, state) {
    var ch = stream.next();

    // call hooks from the mime type
    if (hooks[ch]) {
      var result = hooks[ch](stream, state);
      if (result !== false) return result;
    }

    if (support.hexNumber == true &&
      ((ch == "0" && stream.match(/^[xX][0-9a-fA-F]+/))
      || (ch == "x" || ch == "X") && stream.match(/^'[0-9a-fA-F]+'/))) {
      // hex
      // ref: http://dev.mysql.com/doc/refman/5.5/en/hexadecimal-literals.html
      return "number";
    } else if (support.binaryNumber == true &&
      (((ch == "b" || ch == "B") && stream.match(/^'[01]+'/))
      || (ch == "0" && stream.match(/^b[01]+/)))) {
      // bitstring
      // ref: http://dev.mysql.com/doc/refman/5.5/en/bit-field-literals.html
      return "number";
    } else if (ch.charCodeAt(0) > 47 && ch.charCodeAt(0) < 58) {
      // numbers
      // ref: http://dev.mysql.com/doc/refman/5.5/en/number-literals.html
          stream.match(/^[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?/);
      support.decimallessFloat == true && stream.eat('.');
      return "number";
    } else if (ch == "?" && (stream.eatSpace() || stream.eol() || stream.eat(";"))) {
      // placeholders
      return "variable-3";
    } else if (ch == "'" || (ch == '"' && support.doubleQuote)) {
      // strings
      // ref: http://dev.mysql.com/doc/refman/5.5/en/string-literals.html
      state.tokenize = tokenLiteral(ch);
      return state.tokenize(stream, state);
    } else if ((((support.nCharCast == true && (ch == "n" || ch == "N"))
        || (support.charsetCast == true && ch == "_" && stream.match(/[a-z][a-z0-9]*/i)))
        && (stream.peek() == "'" || stream.peek() == '"'))) {
      // charset casting: _utf8'str', N'str', n'str'
      // ref: http://dev.mysql.com/doc/refman/5.5/en/string-literals.html
      return "keyword";
    } else if (/^[\(\),\;\[\]]/.test(ch)) {
      // no highlightning
      return null;
    } else if (support.commentSlashSlash && ch == "/" && stream.eat("/")) {
      // 1-line comment
      stream.skipToEnd();
      return "comment";
    } else if ((support.commentHash && ch == "#")
        || (ch == "-" && stream.eat("-") && (!support.commentSpaceRequired || stream.eat(" ")))) {
      // 1-line comments
      // ref: https://kb.askmonty.org/en/comment-syntax/
      stream.skipToEnd();
      return "comment";
    } else if (ch == "/" && stream.eat("*")) {
      // multi-line comments
      // ref: https://kb.askmonty.org/en/comment-syntax/
      state.tokenize = tokenComment;
      return state.tokenize(stream, state);
    } else if (ch == ".") {
      // .1 for 0.1
      if (support.zerolessFloat == true && stream.match(/^(?:\d+(?:e[+-]?\d+)?)/i)) {
        return "number";
      }
      // .table_name (ODBC)
      // // ref: http://dev.mysql.com/doc/refman/5.6/en/identifier-qualifiers.html
      if (support.ODBCdotTable == true && stream.match(/^[a-zA-Z_]+/)) {
        return "variable-2";
      }
    } else if (operatorChars.test(ch)) {
      // operators
      stream.eatWhile(operatorChars);
      return null;
    } else if (ch == '{' &&
        (stream.match(/^( )*(d|D|t|T|ts|TS)( )*'[^']*'( )*}/) || stream.match(/^( )*(d|D|t|T|ts|TS)( )*"[^"]*"( )*}/))) {
      // dates (weird ODBC syntax)
      // ref: http://dev.mysql.com/doc/refman/5.5/en/date-and-time-literals.html
      return "number";
    } else {
      stream.eatWhile(/^[_\w\d]/);
      var word = stream.current().toLowerCase();
      // dates (standard SQL syntax)
      // ref: http://dev.mysql.com/doc/refman/5.5/en/date-and-time-literals.html
      if (dateSQL.hasOwnProperty(word) && (stream.match(/^( )+'[^']*'/) || stream.match(/^( )+"[^"]*"/)))
        return "number";
      if (atoms.hasOwnProperty(word)) return "atom";
      if (builtin.hasOwnProperty(word)) return "builtin";
      if (keywords.hasOwnProperty(word)) return "keyword";
      if (client.hasOwnProperty(word)) return "string-2";
      return null;
    }
  }

  // 'string', with char specified in quote escaped by '\'
  function tokenLiteral(quote) {
    return function(stream, state) {
      var escaped = false, ch;
      while ((ch = stream.next()) != null) {
        if (ch == quote && !escaped) {
          state.tokenize = tokenBase;
          break;
        }
        escaped = !escaped && ch == "\\";
      }
      return "string";
    };
  }
  function tokenComment(stream, state) {
    while (true) {
      if (stream.skipTo("*")) {
        stream.next();
        if (stream.eat("/")) {
          state.tokenize = tokenBase;
          break;
        }
      } else {
        stream.skipToEnd();
        break;
      }
    }
    return "comment";
  }

  function pushContext(stream, state, type) {
    state.context = {
      prev: state.context,
      indent: stream.indentation(),
      col: stream.column(),
      type: type
    };
  }

  function popContext(state) {
    state.indent = state.context.indent;
    state.context = state.context.prev;
  }

  return {
    startState: function() {
      return {tokenize: tokenBase, context: null};
    },

    token: function(stream, state) {
      if (stream.sol()) {
        if (state.context && state.context.align == null)
          state.context.align = false;
      }
      if (stream.eatSpace()) return null;

      var style = state.tokenize(stream, state);
      if (style == "comment") return style;

      if (state.context && state.context.align == null)
        state.context.align = true;

      var tok = stream.current();
      if (tok == "(")
        pushContext(stream, state, ")");
      else if (tok == "[")
        pushContext(stream, state, "]");
      else if (state.context && state.context.type == tok)
        popContext(state);
      return style;
    },

    indent: function(state, textAfter) {
      var cx = state.context;
      if (!cx) return CodeMirror.Pass;
      var closing = textAfter.charAt(0) == cx.type;
      if (cx.align) return cx.col + (closing ? 0 : 1);
      else return cx.indent + (closing ? 0 : config.indentUnit);
    },

    blockCommentStart: "/*",
    blockCommentEnd: "*/",
    lineComment: support.commentSlashSlash ? "//" : support.commentHash ? "#" : null
  };
});

(function() {
  "use strict";

  // `identifier`
  function hookIdentifier(stream) {
    // MySQL/MariaDB identifiers
    // ref: http://dev.mysql.com/doc/refman/5.6/en/identifier-qualifiers.html
    var ch;
    while ((ch = stream.next()) != null) {
      if (ch == "`" && !stream.eat("`")) return "variable-2";
    }
    stream.backUp(stream.current().length - 1);
    return stream.eatWhile(/\w/) ? "variable-2" : null;
  }

  // variable token
  function hookVar(stream) {
    // variables
    // @@prefix.varName @varName
    // varName can be quoted with ` or ' or "
    // ref: http://dev.mysql.com/doc/refman/5.5/en/user-variables.html
    if (stream.eat("@")) {
      stream.match(/^session\./);
      stream.match(/^local\./);
      stream.match(/^global\./);
    }

    if (stream.eat("'")) {
      stream.match(/^.*'/);
      return "variable-2";
    } else if (stream.eat('"')) {
      stream.match(/^.*"/);
      return "variable-2";
    } else if (stream.eat("`")) {
      stream.match(/^.*`/);
      return "variable-2";
    } else if (stream.match(/^[0-9a-zA-Z$\.\_]+/)) {
      return "variable-2";
    }
    return null;
  };

  // short client keyword token
  function hookClient(stream) {
    // \N means NULL
    // ref: http://dev.mysql.com/doc/refman/5.5/en/null-values.html
    if (stream.eat("N")) {
        return "atom";
    }
    // \g, etc
    // ref: http://dev.mysql.com/doc/refman/5.5/en/mysql-commands.html
    return stream.match(/^[a-zA-Z.#!?]/) ? "variable-2" : null;
  }

  // these keywords are used by all SQL dialects (however, a mode can still overwrite it)
  var sqlKeywords = "alter and as asc between by count create delete desc distinct drop from having in insert into is join like not on or order select set table union update values where ";

  // turn a space-separated list into an array
  function set(str) {
    var obj = {}, words = str.split(" ");
    for (var i = 0; i < words.length; ++i) obj[words[i]] = true;
    return obj;
  }

  // A generic SQL Mode. It's not a standard, it just try to support what is generally supported
  CodeMirror.defineMIME("text/x-sql", {
    name: "sql",
    keywords: set(sqlKeywords + "begin"),
    builtin: set("bool boolean bit blob enum long longblob longtext medium mediumblob mediumint mediumtext time timestamp tinyblob tinyint tinytext text bigint int int1 int2 int3 int4 int8 integer float float4 float8 double char varbinary varchar varcharacter precision real date datetime year unsigned signed decimal numeric"),
    atoms: set("false true null unknown"),
    operatorChars: /^[*+\-%<>!=]/,
    dateSQL: set("date time timestamp"),
    support: set("ODBCdotTable doubleQuote binaryNumber hexNumber")
  });

  CodeMirror.defineMIME("text/x-mssql", {
    name: "sql",
    client: set("charset clear connect edit ego exit go help nopager notee nowarning pager print prompt quit rehash source status system tee"),
    keywords: set(sqlKeywords + "begin trigger proc view index for add constraint key primary foreign collate clustered nonclustered"),
    builtin: set("bigint numeric bit smallint decimal smallmoney int tinyint money float real char varchar text nchar nvarchar ntext binary varbinary image cursor timestamp hierarchyid uniqueidentifier sql_variant xml table "),
    atoms: set("false true null unknown"),
    operatorChars: /^[*+\-%<>!=]/,
    dateSQL: set("date datetimeoffset datetime2 smalldatetime datetime time"),
    hooks: {
      "@":   hookVar
    }
  });

  CodeMirror.defineMIME("text/x-mysql", {
    name: "sql",
    client: set("charset clear connect edit ego exit go help nopager notee nowarning pager print prompt quit rehash source status system tee"),
    keywords: set(sqlKeywords + "accessible action add after algorithm all analyze asensitive at authors auto_increment autocommit avg avg_row_length before binary binlog both btree cache call cascade cascaded case catalog_name chain change changed character check checkpoint checksum class_origin client_statistics close coalesce code collate collation collations column columns comment commit committed completion concurrent condition connection consistent constraint contains continue contributors convert cross current current_date current_time current_timestamp current_user cursor data database databases day_hour day_microsecond day_minute day_second deallocate dec declare default delay_key_write delayed delimiter des_key_file describe deterministic dev_pop dev_samp deviance diagnostics directory disable discard distinctrow div dual dumpfile each elseif enable enclosed end ends engine engines enum errors escape escaped even event events every execute exists exit explain extended fast fetch field fields first flush for force foreign found_rows full fulltext function general get global grant grants group groupby_concat handler hash help high_priority hosts hour_microsecond hour_minute hour_second if ignore ignore_server_ids import index index_statistics infile inner innodb inout insensitive insert_method install interval invoker isolation iterate key keys kill language last leading leave left level limit linear lines list load local localtime localtimestamp lock logs low_priority master master_heartbeat_period master_ssl_verify_server_cert masters match max max_rows maxvalue message_text middleint migrate min min_rows minute_microsecond minute_second mod mode modifies modify mutex mysql_errno natural next no no_write_to_binlog offline offset one online open optimize option optionally out outer outfile pack_keys parser partition partitions password phase plugin plugins prepare preserve prev primary privileges procedure processlist profile profiles purge query quick range read read_write reads real rebuild recover references regexp relaylog release remove rename reorganize repair repeatable replace require resignal restrict resume return returns revoke right rlike rollback rollup row row_format rtree savepoint schedule schema schema_name schemas second_microsecond security sensitive separator serializable server session share show signal slave slow smallint snapshot soname spatial specific sql sql_big_result sql_buffer_result sql_cache sql_calc_found_rows sql_no_cache sql_small_result sqlexception sqlstate sqlwarning ssl start starting starts status std stddev stddev_pop stddev_samp storage straight_join subclass_origin sum suspend table_name table_statistics tables tablespace temporary terminated to trailing transaction trigger triggers truncate uncommitted undo uninstall unique unlock upgrade usage use use_frm user user_resources user_statistics using utc_date utc_time utc_timestamp value variables varying view views warnings when while with work write xa xor year_month zerofill begin do then else loop repeat"),
    builtin: set("bool boolean bit blob decimal double float long longblob longtext medium mediumblob mediumint mediumtext time timestamp tinyblob tinyint tinytext text bigint int int1 int2 int3 int4 int8 integer float float4 float8 double char varbinary varchar varcharacter precision date datetime year unsigned signed numeric"),
    atoms: set("false true null unknown"),
    operatorChars: /^[*+\-%<>!=&|^]/,
    dateSQL: set("date time timestamp"),
    support: set("ODBCdotTable decimallessFloat zerolessFloat binaryNumber hexNumber doubleQuote nCharCast charsetCast commentHash commentSpaceRequired"),
    hooks: {
      "@":   hookVar,
      "`":   hookIdentifier,
      "\\":  hookClient
    }
  });

  CodeMirror.defineMIME("text/x-mariadb", {
    name: "sql",
    client: set("charset clear connect edit ego exit go help nopager notee nowarning pager print prompt quit rehash source status system tee"),
    keywords: set(sqlKeywords + "accessible action add after algorithm all always analyze asensitive at authors auto_increment autocommit avg avg_row_length before binary binlog both btree cache call cascade cascaded case catalog_name chain change changed character check checkpoint checksum class_origin client_statistics close coalesce code collate collation collations column columns comment commit committed completion concurrent condition connection consistent constraint contains continue contributors convert cross current current_date current_time current_timestamp current_user cursor data database databases day_hour day_microsecond day_minute day_second deallocate dec declare default delay_key_write delayed delimiter des_key_file describe deterministic dev_pop dev_samp deviance diagnostics directory disable discard distinctrow div dual dumpfile each elseif enable enclosed end ends engine engines enum errors escape escaped even event events every execute exists exit explain extended fast fetch field fields first flush for force foreign found_rows full fulltext function general generated get global grant grants group groupby_concat handler hard hash help high_priority hosts hour_microsecond hour_minute hour_second if ignore ignore_server_ids import index index_statistics infile inner innodb inout insensitive insert_method install interval invoker isolation iterate key keys kill language last leading leave left level limit linear lines list load local localtime localtimestamp lock logs low_priority master master_heartbeat_period master_ssl_verify_server_cert masters match max max_rows maxvalue message_text middleint migrate min min_rows minute_microsecond minute_second mod mode modifies modify mutex mysql_errno natural next no no_write_to_binlog offline offset one online open optimize option optionally out outer outfile pack_keys parser partition partitions password persistent phase plugin plugins prepare preserve prev primary privileges procedure processlist profile profiles purge query quick range read read_write reads real rebuild recover references regexp relaylog release remove rename reorganize repair repeatable replace require resignal restrict resume return returns revoke right rlike rollback rollup row row_format rtree savepoint schedule schema schema_name schemas second_microsecond security sensitive separator serializable server session share show shutdown signal slave slow smallint snapshot soft soname spatial specific sql sql_big_result sql_buffer_result sql_cache sql_calc_found_rows sql_no_cache sql_small_result sqlexception sqlstate sqlwarning ssl start starting starts status std stddev stddev_pop stddev_samp storage straight_join subclass_origin sum suspend table_name table_statistics tables tablespace temporary terminated to trailing transaction trigger triggers truncate uncommitted undo uninstall unique unlock upgrade usage use use_frm user user_resources user_statistics using utc_date utc_time utc_timestamp value variables varying view views virtual warnings when while with work write xa xor year_month zerofill begin do then else loop repeat"),
    builtin: set("bool boolean bit blob decimal double float long longblob longtext medium mediumblob mediumint mediumtext time timestamp tinyblob tinyint tinytext text bigint int int1 int2 int3 int4 int8 integer float float4 float8 double char varbinary varchar varcharacter precision date datetime year unsigned signed numeric"),
    atoms: set("false true null unknown"),
    operatorChars: /^[*+\-%<>!=&|^]/,
    dateSQL: set("date time timestamp"),
    support: set("ODBCdotTable decimallessFloat zerolessFloat binaryNumber hexNumber doubleQuote nCharCast charsetCast commentHash commentSpaceRequired"),
    hooks: {
      "@":   hookVar,
      "`":   hookIdentifier,
      "\\":  hookClient
    }
  });

  // the query language used by Apache Cassandra is called CQL, but this mime type
  // is called Cassandra to avoid confusion with Contextual Query Language
  CodeMirror.defineMIME("text/x-cassandra", {
    name: "sql",
    client: { },
    keywords: set("use select from using consistency where limit first reversed first and in insert into values using consistency ttl update set delete truncate begin batch apply create keyspace with columnfamily primary key index on drop alter type add any one quorum all local_quorum each_quorum"),
    builtin: set("ascii bigint blob boolean counter decimal double float int text timestamp uuid varchar varint"),
    atoms: set("false true"),
    operatorChars: /^[<>=]/,
    dateSQL: { },
    support: set("commentSlashSlash decimallessFloat"),
    hooks: { }
  });

  // this is based on Peter Raganitsch's 'plsql' mode
  CodeMirror.defineMIME("text/x-plsql", {
    name:       "sql",
    client:     set("appinfo arraysize autocommit autoprint autorecovery autotrace blockterminator break btitle cmdsep colsep compatibility compute concat copycommit copytypecheck define describe echo editfile embedded escape exec execute feedback flagger flush heading headsep instance linesize lno loboffset logsource long longchunksize markup native newpage numformat numwidth pagesize pause pno recsep recsepchar release repfooter repheader serveroutput shiftinout show showmode size spool sqlblanklines sqlcase sqlcode sqlcontinue sqlnumber sqlpluscompatibility sqlprefix sqlprompt sqlterminator suffix tab term termout time timing trimout trimspool ttitle underline verify version wrap"),
    keywords:   set("abort accept access add all alter and any array arraylen as asc assert assign at attributes audit authorization avg base_table begin between binary_integer body boolean by case cast char char_base check close cluster clusters colauth column comment commit compress connect connected constant constraint crash create current currval cursor data_base database date dba deallocate debugoff debugon decimal declare default definition delay delete desc digits dispose distinct do drop else elseif elsif enable end entry escape exception exception_init exchange exclusive exists exit external fast fetch file for force form from function generic goto grant group having identified if immediate in increment index indexes indicator initial initrans insert interface intersect into is key level library like limited local lock log logging long loop master maxextents maxtrans member minextents minus mislabel mode modify multiset new next no noaudit nocompress nologging noparallel not nowait number_base object of off offline on online only open option or order out package parallel partition pctfree pctincrease pctused pls_integer positive positiven pragma primary prior private privileges procedure public raise range raw read rebuild record ref references refresh release rename replace resource restrict return returning returns reverse revoke rollback row rowid rowlabel rownum rows run savepoint schema segment select separate session set share snapshot some space split sql start statement storage subtype successful synonym tabauth table tables tablespace task terminate then to trigger truncate type union unique unlimited unrecoverable unusable update use using validate value values variable view views when whenever where while with work"),
    builtin:    set("abs acos add_months ascii asin atan atan2 average bfile bfilename bigserial bit blob ceil character chartorowid chr clob concat convert cos cosh count dec decode deref dual dump dup_val_on_index empty error exp false float floor found glb greatest hextoraw initcap instr instrb int integer isopen last_day least lenght lenghtb ln lower lpad ltrim lub make_ref max min mlslabel mod months_between natural naturaln nchar nclob new_time next_day nextval nls_charset_decl_len nls_charset_id nls_charset_name nls_initcap nls_lower nls_sort nls_upper nlssort no_data_found notfound null number numeric nvarchar2 nvl others power rawtohex real reftohex round rowcount rowidtochar rowtype rpad rtrim serial sign signtype sin sinh smallint soundex sqlcode sqlerrm sqrt stddev string substr substrb sum sysdate tan tanh to_char text to_date to_label to_multi_byte to_number to_single_byte translate true trunc uid unlogged upper user userenv varchar varchar2 variance varying vsize xml"),
    operatorChars: /^[*+\-%<>!=~]/,
    dateSQL:    set("date time timestamp"),
    support:    set("doubleQuote nCharCast zerolessFloat binaryNumber hexNumber")
  });

  // Created to support specific hive keywords
  CodeMirror.defineMIME("text/x-hive", {
    name: "sql",
    keywords: set("select alter $elem$ $key$ $value$ add after all analyze and archive as asc before between binary both bucket buckets by cascade case cast change cluster clustered clusterstatus collection column columns comment compute concatenate continue create cross cursor data database databases dbproperties deferred delete delimited desc describe directory disable distinct distribute drop else enable end escaped exclusive exists explain export extended external false fetch fields fileformat first format formatted from full function functions grant group having hold_ddltime idxproperties if import in index indexes inpath inputdriver inputformat insert intersect into is items join keys lateral left like limit lines load local location lock locks mapjoin materialized minus msck no_drop nocompress not of offline on option or order out outer outputdriver outputformat overwrite partition partitioned partitions percent plus preserve procedure purge range rcfile read readonly reads rebuild recordreader recordwriter recover reduce regexp rename repair replace restrict revoke right rlike row schema schemas semi sequencefile serde serdeproperties set shared show show_database sort sorted ssl statistics stored streamtable table tables tablesample tblproperties temporary terminated textfile then tmp to touch transform trigger true unarchive undo union uniquejoin unlock update use using utc utc_tmestamp view when where while with"),
    builtin: set("bool boolean long timestamp tinyint smallint bigint int float double date datetime unsigned string array struct map uniontype"),
    atoms: set("false true null unknown"),
    operatorChars: /^[*+\-%<>!=]/,
    dateSQL: set("date timestamp"),
    support: set("ODBCdotTable doubleQuote binaryNumber hexNumber")
  });
}());

});

/*
  How Properties of Mime Types are used by SQL Mode
  =================================================

  keywords:
    A list of keywords you want to be highlighted.
  builtin:
    A list of builtin types you want to be highlighted (if you want types to be of class "builtin" instead of "keyword").
  operatorChars:
    All characters that must be handled as operators.
  client:
    Commands parsed and executed by the client (not the server).
  support:
    A list of supported syntaxes which are not common, but are supported by more than 1 DBMS.
    * ODBCdotTable: .tableName
    * zerolessFloat: .1
    * doubleQuote
    * nCharCast: N'string'
    * charsetCast: _utf8'string'
    * commentHash: use # char for comments
    * commentSlashSlash: use // for comments
    * commentSpaceRequired: require a space after -- for comments
  atoms:
    Keywords that must be highlighted as atoms,. Some DBMS's support more atoms than others:
    UNKNOWN, INFINITY, UNDERFLOW, NaN...
  dateSQL:
    Used for date/time SQL standard syntax, because not all DBMS's support same temporal types.
*/
