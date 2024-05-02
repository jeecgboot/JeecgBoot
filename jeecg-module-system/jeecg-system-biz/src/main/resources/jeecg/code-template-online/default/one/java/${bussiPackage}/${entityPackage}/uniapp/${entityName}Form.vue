<template>
    <view>
        <!--标题和返回-->
		<cu-custom :bgColor="NavBarColor" isBack :backRouterName="backRouteName">
			<block slot="backText">返回</block>
			<block slot="content">${tableVo.ftlDescription}</block>
		</cu-custom>
		 <!--表单区域-->
		<view>
			<form>
    <#list columns as po><#rt/>
    <#if po.fieldName !='id'><#rt/>
         <#if po.classType =='date'>
              <my-date label="${po.filedComment}：" fields="day" v-model="model.${po.fieldName}" placeholder="请输入${po.filedComment}"></my-date>
         <#elseif po.classType =='datetime'>
              <my-date label="${po.filedComment}：" v-model="model.${po.fieldName}" placeholder="请输入${po.filedComment}"></my-date>
         <#else>
              <view class="cu-form-group">
                <view class="flex align-center">
                  <view class="title"><text space="ensp">${po.filedComment}：</text></view>
                  <input <#if "int,BigDecimal,double,"?contains(po.fieldDbType)>type="number"</#if> placeholder="请输入${po.filedComment}" v-model="model.${po.fieldName}"/>
                </view>
              </view>
        </#if>
    </#if>
    </#list>
				<view class="padding">
					<button class="cu-btn block bg-blue margin-tb-sm lg" @click="onSubmit">
						<text v-if="loading" class="cuIcon-loading2 cuIconfont-spin"></text>提交
					</button>
				</view>
			</form>
		</view>
    </view>
</template>

<script>
    import myDate from '@/components/my-componets/my-date.vue'

    export default {
        name: "${entityName}Form",
        components:{ myDate },
        props:{
          formData:{
              type:Object,
              default:()=>{},
              required:false
          }
        },
        data(){
            return {
				CustomBar: this.CustomBar,
				NavBarColor: this.NavBarColor,
				loading:false,
                model: {},
                backRouteName:'index',
                url: {
                  queryById: "/${entityPackage}/${entityName?uncap_first}/queryById",
                  add: "/${entityPackage}/${entityName?uncap_first}/add",
                  edit: "/${entityPackage}/${entityName?uncap_first}/edit",
                },
            }
        },
        created(){
             this.initFormData();
        },
        methods:{
           initFormData(){
               if(this.formData){
                    let dataId = this.formData.dataId;
                    this.$http.get(this.url.queryById,{params:{id:dataId}}).then((res)=>{
                        if(res.data.success){
                            console.log("表单数据",res);
                            this.model = res.data.result;
                        }
                    })
                }
            },
            onSubmit() {
                let myForm = {...this.model};
                this.loading = true;
                let url = myForm.id?this.url.edit:this.url.add;
				this.$http.post(url,myForm).then(res=>{
				   console.log("res",res)
				   this.loading = false
				   this.$Router.push({name:this.backRouteName})
				}).catch(()=>{
					this.loading = false
				});
            }
        }
    }
</script>
