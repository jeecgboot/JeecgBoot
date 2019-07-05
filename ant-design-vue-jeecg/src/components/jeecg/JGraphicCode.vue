<template>
  <div class="gc-canvas" @click="reloadPic">
    <canvas id="gc-canvas" :width="contentWidth" :height="contentHeight"></canvas>
  </div>
</template>

<script>
  export default {
    name: 'JGraphicCode',
    props: {
      length:{
        type: Number,
        default: 4
      },
      fontSizeMin: {
        type: Number,
        default: 20
      },
      fontSizeMax: {
        type: Number,
        default: 45
      },
      backgroundColorMin: {
        type: Number,
        default: 180
      },
      backgroundColorMax: {
        type: Number,
        default: 240
      },
      colorMin: {
        type: Number,
        default: 50
      },
      colorMax: {
        type: Number,
        default: 160
      },
      lineColorMin: {
        type: Number,
        default: 40
      },
      lineColorMax: {
        type: Number,
        default: 180
      },
      dotColorMin: {
        type: Number,
        default: 0
      },
      dotColorMax: {
        type: Number,
        default: 255
      },
      contentWidth: {
        type: Number,
        default:136
      },
      contentHeight: {
        type: Number,
        default: 38
      }
    },
    methods: {
      // 生成一个随机数
      randomNum (min, max) {
        return Math.floor(Math.random() * (max - min) + min)
      },
      // 生成一个随机的颜色
      randomColor (min, max) {
        let r = this.randomNum(min, max)
        let g = this.randomNum(min, max)
        let b = this.randomNum(min, max)
        return 'rgb(' + r + ',' + g + ',' + b + ')'
      },
      drawPic () {
        this.randomCode()
        let canvas = document.getElementById('gc-canvas')
        let ctx = canvas.getContext('2d')
        ctx.textBaseline = 'bottom'
        // 绘制背景
        ctx.fillStyle = this.randomColor(this.backgroundColorMin, this.backgroundColorMax)
        ctx.fillRect(0, 0, this.contentWidth, this.contentHeight)
        // 绘制文字
        for (let i = 0; i < this.code.length; i++) {
          this.drawText(ctx, this.code[i], i)
        }
        this.drawLine(ctx)
        this.drawDot(ctx)
        this.$emit("success",this.code)
      },
      drawText (ctx, txt, i) {
        ctx.fillStyle = this.randomColor(this.colorMin, this.colorMax)
        let fontSize = this.randomNum(this.fontSizeMin, this.fontSizeMax)
        ctx.font = fontSize + 'px SimHei'
        let padding = 10;
        let offset = (this.contentWidth-40)/(this.code.length-1)
        let x=padding;
        if(i>0){
          x = padding+(i*offset)
        }
        //let x = (i + 1) * (this.contentWidth / (this.code.length + 1))
        let y = this.randomNum(this.fontSizeMax, this.contentHeight - 5)
        if(fontSize>40){
          y=40
        }
        var deg = this.randomNum(-10,10)
        // 修改坐标原点和旋转角度
        ctx.translate(x, y)
        ctx.rotate(deg * Math.PI / 180)
        ctx.fillText(txt, 0, 0)
        // 恢复坐标原点和旋转角度
        ctx.rotate(-deg * Math.PI / 180)
        ctx.translate(-x, -y)
      },
      drawLine (ctx) {
        // 绘制干扰线
        for (let i = 0; i <1; i++) {
          ctx.strokeStyle = this.randomColor(this.lineColorMin, this.lineColorMax)
          ctx.beginPath()
          ctx.moveTo(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight))
          ctx.lineTo(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight))
          ctx.stroke()
        }
      },
      drawDot (ctx) {
        // 绘制干扰点
        for (let i = 0; i < 100; i++) {
          ctx.fillStyle = this.randomColor(0, 255)
          ctx.beginPath()
          ctx.arc(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight), 1, 0, 2 * Math.PI)
          ctx.fill()
        }
      },
      reloadPic(){
        this.drawPic()
      },
      randomCode(){
        let random = ''
        //去掉了I l i o O
        let str = "QWERTYUPLKJHGFDSAZXCVBNMqwertyupkjhgfdsazxcvbnm1234567890"
        for(let i = 0; i < this.length; i++) {
          let index = Math.floor(Math.random()*57);
          random += str[index];
        }
        this.code = random
      }
    },
    mounted () {
      this.drawPic()
    },
    data(){
      return {
        code:""
      }
    }

  }
</script>

<style scoped>

</style>