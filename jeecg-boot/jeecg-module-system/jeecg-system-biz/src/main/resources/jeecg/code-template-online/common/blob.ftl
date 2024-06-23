<#if po.fieldDbType=='Blob'>
    private transient java.lang.String ${po.fieldName}String;

    private byte[] ${po.fieldName};

    public byte[] get${po.fieldName?cap_first}(){
        if(${po.fieldName}String==null){
            return null;
        }
        try {
            return ${po.fieldName}String.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get${po.fieldName?cap_first}String(){
        if(${po.fieldName}==null || ${po.fieldName}.length==0){
            return "";
        }
        try {
            return new String(${po.fieldName},"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
<#else>
    @ApiModelProperty(value = "${po.filedComment}")
  <#if po.fieldDbName == 'del_flag'>
    @TableLogic
  </#if>
    private ${po.fieldType} ${po.fieldName};
</#if>