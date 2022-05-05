import { formatDate } from '@/utils/util'
import Area from '@/components/_util/Area'
import { postAction } from '@/api/manage'

const onlUtil = {
  data(){
    return {
      mixin_pca:'',
      flowCodePre: 'onl_'
    }
  },
  created(){
    this.mixin_pca = new Area(this.$Jpcaa)
  },
  methods:{
    simpleDateFormat(millisecond, format){
      return formatDate(millisecond, format)
    },
    getPcaText(code){
      return this.mixin_pca.getText(code);
    },
    getPcaCode(text){
      return this.mixin_pca.getCode(text)
    }
  }
}

export  { onlUtil }