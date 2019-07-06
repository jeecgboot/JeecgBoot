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
    isDisabledAuth(code){
      return disabledAuthFilter(code,this.formData);
    },
  }

}