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
  methods:{
    isDisabledAuth(code){
      return disabledAuthFilter(code,this.formData);
    },
  }

}