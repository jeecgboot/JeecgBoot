import { formatDate } from '@/utils/util'
import Area from '@/components/_util/Area'

const onlUtil = {
  data(){
    return {
      mixin_pca:''
    }
  },
  created(){
    this.mixin_pca = new Area()
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