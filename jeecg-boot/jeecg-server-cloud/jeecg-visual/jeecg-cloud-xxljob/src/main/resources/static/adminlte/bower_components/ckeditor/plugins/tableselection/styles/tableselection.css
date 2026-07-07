.cke_table-faked-selection-editor *::selection, table[data-cke-table-faked-selection-table] *::selection {
	background: transparent;
}

.cke_table-faked-selection {
	background: darkgray !important;
	color: black;
}
.cke_table-faked-selection a {
	color: black;
}
.cke_editable:focus .cke_table-faked-selection {
	/* We have to use !important here, as td might specify it's own background, thus table selection
	would not be visible. */
	background: #0076cb !important;
	color: white;
}
.cke_editable:focus .cke_table-faked-selection a {
	color: white;
}
.cke_table-faked-selection::-moz-selection, .cke_table-faked-selection ::-moz-selection {
	background: transparent;
}
.cke_table-faked-selection::selection, .cke_table-faked-selection ::selection {
	background: transparent;
}

/* Change the cursor when selecting cells (#706).
 *
 * This solution does not work in IE, Edge and Safari due to upstream isues:
 * https://developer.microsoft.com/en-us/microsoft-edge/platform/issues/3419602/
 * https://bugs.webkit.org/show_bug.cgi?id=53341
 */
table[data-cke-table-faked-selection-table] {
	cursor: cell;
}
