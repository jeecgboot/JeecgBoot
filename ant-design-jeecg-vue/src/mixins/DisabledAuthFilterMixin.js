/**
 *
 */
import { disabledAuthFilter } from "@/utils/authFilter"

export const DisabledAuthFilterMixin = {
  props: ['formData'],
  data(){
    return {

    }
  },
  created() {

  },
  methods:{
    disabledAuth(code){
      return disabledAuthFilter(code,this.formData);
    },
  }

}