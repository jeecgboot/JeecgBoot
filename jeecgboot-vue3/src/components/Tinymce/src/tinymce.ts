// Any plugins you want to setting has to be imported
// Detail plugins list see https://www.tinymce.com/docs/plugins/
// Custom builds see https://www.tinymce.com/download/custom-builds/
// colorpicker/contextmenu/textcolor plugin is now built in to the core editor, please remove it from your editor configuration

export const plugins = [
  'advlist anchor autolink autosave code codesample  directionality  fullscreen hr insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace tabfocus  template  textpattern visualblocks visualchars wordcount image',
];

export const toolbar =
  'fullscreen code preview | undo redo | bold italic underline strikethrough | fontselect fontsizeselect formatselect | alignleft aligncenter alignright alignjustify | outdent indent lineheight|subscript superscript blockquote| numlist bullist checklist | forecolor backcolor casechange permanentpen formatpainter removeformat | pagebreak | charmap emoticons   | insertfile image media pageembed  link anchor  codesample insertdatetime hr| a11ycheck ltr rtl';

export const simplePlugins = 'lists image link fullscreen';

export const simpleToolbar = [
  'undo redo styles  bold italic alignleft aligncenter alignright alignjustify bullist numlist outdent indent lists image link fullscreen',
];

export const menubar = 'file edit insert view format table';
