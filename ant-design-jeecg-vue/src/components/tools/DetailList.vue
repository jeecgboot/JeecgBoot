<template>
  <div :class="['detail-list', size, layout === 'vertical' ? 'vertical': 'horizontal']">
    <div v-if="title" class="title">{{ title }}</div>
    <a-row>
      <slot></slot>
    </a-row>
  </div>
</template>

<script>
  import { Col } from 'ant-design-vue/es/grid/'

  const Item = {
    name: 'DetailListItem',
    props: {
      term: {
        type: String,
        default: '',
        required: false
      },
    },
    inject: {
      col: {
        type: Number
      }
    },
    render () {
      return (
        <Col {...{props: responsive[this.col]}}>
          <div class="term">{this.$props.term}</div>
          <div class="content">{this.$slots.default}</div>
        </Col>
      )
    }
  }

  const responsive = {
    1: { xs: 24 },
    2: { xs: 24, sm: 12 },
    3: { xs: 24, sm: 12, md: 8 },
    4: { xs: 24, sm: 12, md: 6 }
  }

  export default {
    name: "DetailList",
    Item: Item,
    components: {
      Col
    },
    props: {
      title: {
        type: String,
        default: '',
        required: false
      },
      col: {
        type: Number,
        required: false,
        default: 3
      },
      size: {
        type: String,
        required: false,
        default: 'large'
      },
      layout: {
        type: String,
        required: false,
        default: 'horizontal'
      }
    },
    provide () {
      return {
        col: this.col > 4 ? 4 : this.col
      }
    }
  }
</script>

<style lang="scss">

  .detail-list {

    .title {
      color: rgba(0,0,0,.85);
      font-size: 14px;
      font-weight: 500;
      margin-bottom: 16px;
    }

    .term {
      color: rgba(0,0,0,.85);
      display: table-cell;
      line-height: 20px;
      margin-right: 8px;
      padding-bottom: 16px;
      white-space: nowrap;

      &:after {
        content: ":";
        margin: 0 8px 0 2px;
        position: relative;
        top: -.5px;
      }
    }

    .content {
      color: rgba(0,0,0,.65);
      display: table-cell;
      line-height: 22px;
      padding-bottom: 16px;
      width: 100%;
    }

    &.small {

      .title {
        font-size: 14px;
        color: rgba(0, 0, 0, .65);
        font-weight: normal;
        margin-bottom: 12px;
      }
      .term, .content {
        padding-bottom: 8px;
      }
    }

    &.large {
      .term, .content {
        padding-bottom: 16px;
      }

      .title {
        font-size: 16px;
      }
    }

    &.vertical {
      .term {
        padding-bottom: 8px;
      }
      .term, .content {
        display: block;
      }
    }
  }
</style>