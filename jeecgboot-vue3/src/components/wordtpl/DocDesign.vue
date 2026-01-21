<template>
  <Loading :loading="loading" content="加载中请稍后"></Loading>
  <div class="content" style="height: 100vh; display: flex; flex-direction: column; overflow: hidden; background-color: #f0f2f5">
    <div style="width: 100%; flex: none" class="header-box">
      <a-layout>
        <a-layout-header class="space-between header">
          <div class="align-center header-title">
            <div class="header-name ellipsis" style="cursor: pointer" :title="formState.name" @click="showInputClick" v-if="!showInput">
              {{ formState.name }}
            </div>
            <div v-else class="header-name">
              <a-input ref="nameRef" style="border-bottom: 1px solid #73a2e3; border-radius: 0" :bordered="false" v-model:value="formState.name" @pressEnter="saveTemplateNameClick" @blur="saveTemplateNameClick"></a-input>
            </div>
          </div>
        </a-layout-header>
        <a-layout-content>
          <div class="center">
            <div class="menu" editor-component="menu">
              <div class="menu-item">
                <!--  保存  -->
                <div class="jeecg-menu-item-save" title="保存模板" @click="saveTemplate">
                  <i />
                </div>
                <!--  上传  -->
                <div class="jeecg-menu-item-upload" title="上传" @click="uploadWordFile">
                  <i />
                  <input
                    ref="uploadWordFileRef"
                    type="file"
                    accept=".docx"
                    @change="uploadDocxChange"
                    style="display: none"/>
                </div>
                <div style="display: none">
                  <input id="uploadDocxBtn" type="file" accept=".docx" />
                </div>
                <div class="jeecg-menu-item-download" title="导出模板" @click="downloadTemplate">
                  <i />
                </div>
                <!--  预览  -->
                <!-- <div class="menu-item-preview" title="预览">
                   <i />
                 </div>-->
                <div class="jeecg-menu-item-undo no-allow" @click="undo" :title="isApple ? '撤销(⌘+Z)' : '撤销(Ctrl+Z)'">
                  <i />
                </div>
                <div class="jeecg-menu-item-redo no-allow" @click="redo" :title="isApple ? '(重做⌘)' : '(CtrlY)'">
                  <i />
                </div>
                <div class="jeecg-menu-item-painter" title="格式刷(双击可连续使用)" @click="painterClick" @dblclick="painterDblClick">
                  <i />
                </div>
                <div class="jeecg-menu-item-format" title="清除格式" @click="clearFormat">
                  <i />
                </div>
              </div>
              <a-divider type="vertical" style="border-inline-start: 1px solid #cfd2d8; height: 20px" />
              <div class="menu-item">
                <div class="jeecg-menu-item-font" @click="fontFamilyClick('.jeecg-menu-item-font', '.options')">
                  <span class="select" title="字体">{{ formState.family }}</span>
                  <div class="options">
                    <ul>
                      <li v-for="item in fontStyleOption" :data-family="item.value" :style="{ 'font-family': item.value}" @click="font(item.value, item.label)">{{ item.label }}</li>
                    </ul>
                  </div>
                </div>
                <div title="字号" class="jeecg-menu-item-size" @click="fontFamilyClick('.jeecg-menu-item-size', '.options')">
                  <span class="select" title="字体">{{ formState.size }}</span>
                  <div class="options">
                    <ul>
                      <li v-for="item in fontSizeOption" :data-size="item.value" @click="fontSize(item.value, item.label)">{{ item.label }}</li>
                    </ul>
                  </div>
                </div>
                <div class="jeecg-menu-item-size-add" :title="`增大字号(${isApple ? '⌘' : 'Ctrl'}+[)`" @click="sizeAdd">
                  <i />
                </div>
                <div class="jeecg-menu-item-size-minus" :title="`减小字号(${isApple ? '⌘' : 'Ctrl'}+])`" @click="sizeMinus">
                  <i />
                </div>
                <div class="jeecg-menu-item-bold" :title="`加粗(${isApple ? '⌘' : 'Ctrl'}+B)`" @click="blob">
                  <i />
                </div>
                <div class="jeecg-menu-item-italic" :title="`斜体(${isApple ? '⌘' : 'Ctrl'}+I)`" @click="itailc">
                  <i />
                </div>
                <div
                  class="jeecg-menu-item-underline"
                  :title="`下划线(${isApple ? '⌘' : 'Ctrl'}+U)`"
                  @click="fontFamilyClick('.jeecg-menu-item-underline', '.options')"
                >
                  <i @click.stop="underline('')" />
                  <span class="select" />
                  <div class="options">
                    <ul>
                      <li v-for="item in underlineOptions" :data-decoration-style="item.value" @click.stop="underline(item.value)">
                        <i />
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="jeecg-menu-item-strikeout" title="删除线(Ctrl+Shift+X)" @click="strikeout">
                  <i />
                </div>
                <div class="jeecg-menu-item-superscript" :title="`上标(${isApple ? '⌘' : 'Ctrl'}+Shift+,)`" @click="superscript">
                  <i />
                </div>
                <div class="jeecg-menu-item-subscript" :title="`下标(${isApple ? '⌘' : 'Ctrl'}+Shift+.)`" @click="subscript">
                  <i />
                </div>
                <div class="jeecg-menu-item-color" title="字体颜色" @click="colorClick">
                  <i />
                  <span />
                  <input id="color" ref="colorRef" type="color" @input="color">
                </div>
                <div class="jeecg-menu-item-highlight" title="高亮" @click="highLightColorClick">
                  <i />
                  <span />
                  <input id="highlight" type="color">
                </div>
                <div class="jeecg-menu-item-cellcolor" title="单元格颜色" @click="cellColorClick">
                  <i />
                  <!-- <span></span> -->
                  <input ref="cellColorRef" id="cellcolor" type="color" @input="cellColor">
                </div>
              </div>
              <a-divider type="vertical" style="border-inline-start: 1px solid #cfd2d8; height: 20px" />
              <div class="menu-item">
                <div class="jeecg-menu-item-title" @click="fontFamilyClick('.jeecg-menu-item-title', '.options')">
                  <i class="page-mode-i"/>
                  <span class="select" title="切换标题">{{formState.title}}</span>
                  <div class="options">
                    <ul>
                      <li v-for="(item,index) in titleOptions" style="font-size: 16px" :title="`Ctrl+${isApple ? 'Option' : 'Alt'}+${index}`" :data-level="item.value" :style="{fontSize: item.fontSize}" @click="title(item.value)">{{item.label}}</li>
                    </ul>
                  </div>
                </div>
                <div class="jeecg-menu-item-left" title="左对齐" @click="leftAlign" :title="`左对齐(${isApple ? '⌘' : 'Ctrl'}+L)`">
                  <i />
                </div>
                <div class="jeecg-menu-item-center" title="居中" @click="centerAlign" :title="`居中对齐(${isApple ? '⌘' : 'Ctrl'}+E)`">
                  <i />
                </div>
                <div class="jeecg-menu-item-right" title="右对齐" @click="rightAlign" :title="`右对齐(${isApple ? '⌘' : 'Ctrl'}+R)`">
                  <i />
                </div>
                <div class="jeecg-menu-item-alignment" title="两端对齐" @click="alignmentAlign" :title="`两端对齐(${isApple ? '⌘' : 'Ctrl'}+J)`">
                  <i />
                </div>
                <div class="jeecg-menu-item-row-margin" @click="fontFamilyClick('.jeecg-menu-item-row-margin', '.options')">
                  <i title="行间距" class="page-mode-i" />
                  <div class="options">
                    <ul>
                      <li v-for="item in marginOptions" :data-rowmargin="item.value" @click="rowMargin(item.value)">{{item.label}}</li>
                    </ul>
                  </div>
                </div>
                <div class="jeecg-menu-item-list" @click="fontFamilyClick('.jeecg-menu-item-list', '.options')">
                  <i class="page-mode-i" />
                  <div class="options" :title="`列表(${isApple ? '⌘' : 'Ctrl'}+Shift+U)`">
                    <ul>
                      <li  @click="list(null,null)">
                        <span>取消列表</span>
                      </li>
                      <li data-list-type="ol" data-list-style="decimal" @click="list('ol','decimal')">
                        <span>有序列表：</span>
                        <ol>
                          <li>________</li>
                        </ol>
                      </li>
                      <li data-list-type="ul" data-list-style="disc" @click="list('ul','disc')">
                        <span>实心圆点列表：</span>
                        <ul style="list-style-type: disc !important;">
                          <li>________</li>
                        </ul>
                      </li>
                      <li data-list-type="ul" data-list-style="circle" @click="list('ul','circle')">
                        <span>空心圆点列表：</span>
                        <ul style="list-style-type: circle !important;">
                          <li>________</li>
                        </ul>
                      </li>
                      <li data-list-type="ul" data-list-style="square" @click="list('ul','square')">
                        <span>空心方块列表：</span>
                        <ul style="list-style-type: square !important;">
                          <li>________</li>
                        </ul>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <a-divider type="vertical" style="border-inline-start: 1px solid #cfd2d8; height: 20px" />
              <div class="menu-item">
                <!-- <div class="jeecg-menu-item-chart">
                   <i title="图表" />
                 </div>-->
                <div class="jeecg-menu-item-table" @click="tableClick">
                  <i title="表格" class="page-mode-i"/>
                </div>
                <div class="jeecg-menu-item-table-collapse page-mode-i" style="display: none">
                  <div class="table-close" @click="recoveryTable">×</div>
                  <div class="table-title page-mode-i" >
                    <span class="table-select page-mode-i">插入</span>
                    <span class="page-mode-i">表格</span>
                  </div>
                  <div class="table-panel page-mode-i" @mousemove="tableMousemove" @click="tableCellClick"/>
                </div>
                <div class="jeecg-menu-item-image" @click="uploadImage">
                  <i title="图片" />
                  <input
                    ref="uploadImageRef"
                    id="image"
                    type="file"
                    accept=".png, .jpg, .jpeg, .svg, .gif"
                    @change="uploadImageChange"
                  >
                </div>
                <!--<div class="jeecg-menu-item-barcode">
                  <i title="条形码" />
                </div>
                <div class="jeecg-menu-item-qrcode">
                  <i title="二维码" />
                </div>-->
                <div class="jeecg-menu-item-hyperlink" @click="hyperlinkClick">
                  <i title="超链接" />
                </div>
                <div class="jeecg-menu-item-separator" @click.stop="separatorClick">
                  <i title="分割线" class="page-mode-i"/>
                  <div class="options page-mode-i">
                    <ul>
                      <li class="page-mode-i" v-for="item in separatorOption" :data-separator="item.value" @click.stop="separatorActiveClick(item.value)">
                        <i />
                      </li>
                    </ul>
                    <div class="more page-mode-i">
                      <div class="page-mode-i">颜色:<a-input @click.stop="" type="color" class="page-mode-i color" v-model:value="formState.separatorColor"/></div>
                      <div class="page-mode-i">粗细(px):<a-input-number @click.stop="" type="tel" class="page-mode-i lineWidth"  v-model:value="formState.separatorLineWidth"/></div>
                      <div class="page-mode-i" style="margin-top: 10px;">
                        <a-button size="small" type="primary" @click.prevent.stop="separatorOk">确定</a-button>
                        <a-button @click.prevent.stop="separatorCancel" size="small" style="margin-left: 6px">取消</a-button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="jeecg-menu-item-watermark" @click="fontFamilyClick('.jeecg-menu-item-watermark', '.options')">
                  <i title="水印(添加、删除)" class="page-mode-i"/>
                  <div class="options">
                    <ul>
                      <li data-menu="add" @click="addWatermarkClick">添加水印</li>
                      <li data-menu="delete" @click="deleteWatermarkClick">删除水印</li>
                    </ul>
                  </div>
                </div>
                <div class="jeecg-menu-item-page-break" title="分页符" @click="pageBreak">
                  <i />
                </div>
              </div>
              <a-divider type="vertical" style="border-inline-start: 1px solid #cfd2d8; height: 20px" />
              <div class="menu-item">
                <div :title="`搜索与替换(${isApple ? '⌘' : 'Ctrl'}+F)`" class="jeecg-menu-item-search" data-menu="search" title="查找替换" @click="searchClick">
                  <i class="page-mode-i"/>
                </div>
                <div class="jeecg-menu-item-search-collapse" data-menu="search" style="display: none">
                  <div class="jeecg-menu-item-search-collapse-search">
                    <input type="text" @input="searchInput" @keydown="searchKeyDown">
                    <label class="search-result" />
                    <div class="arrow-left" @click="arrowLeft">
                      <i />
                    </div>
                    <div class="arrow-right" @click="arrowRight">
                      <i />
                    </div>
                    <span @click="closeSearch">×</span>
                  </div>
                  <div class="jeecg-menu-item-search-collapse-replace">
                    <input type="text">
                    <button @click="replaceText">替换</button>
                  </div>
                </div>
                <div :title="`打印(${isApple ? '⌘' : 'Ctrl'}+P)`" class="jeecg-menu-item-print" data-menu="print" title="打印" @click="printTemplate">
                  <i />
                </div>
              </div>
            </div>
            <div class="catalog" editor-component="catalog">
              <div class="jeecg-catalog-header">
                <span>目录</span>
                <div class="jeecg-catalog-header-close" @click="closeDirectory">
                  <i />
                </div>
              </div>
              <div class="jeecg-catalog-main" />
            </div>
            <div class="editor-container">
              <div class="jeecg__editor" />
            </div>
          </div>
        </a-layout-content>
        <a-layout-footer>
          <div class="footer" editor-component="footer">
            <div>
              <div class="catalog-mode" title="目录" @click="openDirectory">
                <i />
              </div>
              <div class="page-mode" @click="fontFamilyClick('.page-mode', '.options')">
                <i title="页面模式(分页、连页)" class="page-mode-i"/>
                <div class="options">
                  <ul>
                    <li v-for="item in pageModeOptions" :data-page-mode="item.value" :class="item.value === 'paging'?'active':''" @click="pageMode(item.value)">{{item.label}}</li>
                  </ul>
                </div>
              </div>
              <span>可见页码：<span class="page-no-list">1</span></span>
              <span>页面：<span class="page-no">1</span>/<span class="page-size" >1</span></span>
              <span>字数：<span class="word-count">0</span></span>
            </div>
            <div>
              <div class="page-scale-minus" title="缩小(Ctrl+-)" @click="scaleMiuns">
                <i />
              </div>
              <span
                class="page-scale-percentage"
                title="显示比例(点击可复原Ctrl+0)"
              >100%</span>
              <div class="page-scale-add" title="放大(Ctrl+=)" @click="scaleAdd">
                <i />
              </div>
              <div class="paper-size" @click="fontFamilyClick('.paper-size', '.options')">
                <i title="纸张类型" class="page-mode-i"/>
                <div class="options" title="纸张大小">
                  <ul>
                    <li v-for="item in paperSizeOption" :data-paper-size="item.value" :class="item.label === 'A4'?'active':''" @click="paperSize(item.value)">{{item.label}}</li>
                  </ul>
                </div>
              </div>
              <div class="paper-direction" @click="fontFamilyClick('.paper-direction', '.options')">
                <i title="纸张方向" class="page-mode-i"/>
                <div class="options">
                  <ul>
                    <li v-for="item in paperDirectionOptions" :data-paper-direction="item.value" :class="item.value == 'vertical'?'active':''" @click="paperDirection(item.value)">{{item.label}}</li>
                  </ul>
                </div>
              </div>
              <div class="paper-margin" title="页边距" @click="paperMarginClick">
                <i />
              </div>
              <div class="fullscreen" title="全屏显示">
                <i />
              </div>
            </div>
          </div>
        </a-layout-footer>
      </a-layout>
    </div>
    <!--  页边距  -->
    <PagerMarginDrawer @register="registerPagerMarginDrawer" @ok="setPaperMargin"></PagerMarginDrawer>
    <!--  高亮颜色  -->
    <HighlightColorModal @register="registerHighlightColorModal" @ok="setHighlightColor"></HighlightColorModal>
    <!--  超链接  -->
    <HyperlinkDrawer @register="registerHyperlinkDrawer" @ok="setHyperlink"></HyperlinkDrawer>
    <!--  水印  -->
    <WatermarkDrawer @register="registerWatermarkDrawer" @ok="setWatermark"></WatermarkDrawer>
  </div>
</template>

<script lang="ts" setup>
  import { Loading } from '/@/components/Loading';
  import { nextTick, onUnmounted, ref } from 'vue';
  import { createEditor, method } from '@/components/wordtpl/DocDesign';
  import PagerMarginDrawer from './components/PagerMarginDrawer.vue'
  import HyperlinkDrawer from './components/HyperlinkDrawer.vue'
  import WatermarkDrawer from './components/WatermarkDrawer.vue'
  import HighlightColorModal from './components/HighlightColorModal.vue'
  import { useDrawer } from "@/components/Drawer";
  import { defHttp } from "@/utils/http/axios";
  import { getFileAccessHttpUrl } from "@/utils/common/compUtils";
  import { useModal } from "@/components/Modal";
  
  const props = defineProps({
    //内容（包含图片、页眉、页脚）
    content: { type: Object, default: {} },
    //图片上传路径
    uploadImageUrl: { type: String, default: '/sys/common/upload' },
    //文件上传路径
    uploadFileUrl: { type: String, default: '/wordtpl/template/parse/file' },
  });

  let emit = defineEmits(['download', 'save']);
  
  //字体样式
  const fontStyleOption = [
    { label: '微软雅黑', value: 'Microsoft YaHei' },
    { label: '宋体', value: '宋体' },
    { label: '黑体', value: '黑体' },
    { label: '仿宋', value: '仿宋' },
    { label: '楷体', value: '楷体' },
    { label: '等线', value: '等线' },
    { label: '华文琥珀', value: '华文琥珀' },
    { label: '华文楷体', value: '华文楷体' },
    { label: '华文隶书', value: '华文隶书' },
    { label: '华文新魏', value: '华文新魏' },
    { label: '华文行楷', value: '华文行楷' },
    { label: '华文中宋', value: '华文中宋' },
    { label: '华文彩云', value: '华文彩云' },
    { label: 'Arial', value: 'Arial' },
    { label: 'Segoe UI', value: 'Segoe UI' },
    { label: 'Ink Free', value: 'Ink Free' },
    { label: 'Fantasy', value: 'Fantasy' },
  ];

  //字体
  const fontSizeOption = [
    { label: '初号', value: '56' },{ label: '小初', value: '48' },{ label: '一号', value: '34' },{ label: '小一', value: '32' },
    { label: '二号', value: '29' },{ label: '小二', value: '24' }, { label: '三号', value: '21' }, { label: '小三', value: '20' },
    { label: '四号', value: '18' }, { label: '小四', value: '16' }, { label: '五号', value: '14' },  { label: '小五', value: '12' },
    { label: '六号', value: '10' },{ label: '小六', value: '8' },  { label: '七号', value: '7' },  { label: '八号', value: '6' },
    { label: '5', value: '5' }, { label: '9', value: '9' },{ label: '13', value: '13' },{ label: '15', value: '15' },
    { label: '17', value: '17' }, { label: '19', value: '19' },{ label: '23', value: '23' },{ label: '25', value: '25' },
    { label: '26', value: '26' }, { label: '27', value: '27' },{ label: '30', value: '30' },{ label: '31', value: '31' },
    { label: '33', value: '33' },  { label: '35', value: '35' },{ label: '36', value: '36' },{ label: '37', value: '37' },
    { label: '38', value: '38' }, { label: '39', value: '39' },{ label: '40', value: '40' },{ label: '41', value: '41' },
    { label: '42', value: '42' }, { label: '43', value: '43' },{ label: '44', value: '44' },{ label: '45', value: '45' },
    { label: '46', value: '46' }, { label: '47', value: '47' },{ label: '50', value: '50' },{ label: '51', value: '51' },
    { label: '52', value: '52' }, { label: '52', value: '52' },{ label: '54', value: '54' },{ label: '57', value: '57' },
    { label: '58', value: '58' }, { label: '59', value: '59' },{ label: '60', value: '60' },{ label: '60', value: '60' },
    { label: '62', value: '62' }, { label: '63', value: '63' },{ label: '64', value: '64' },{ label: '65', value: '65' },
    { label: '66', value: '66' },{ label: '67', value: '67' },{ label: '68', value: '68' },{ label: '69', value: '69' },
    { label: '70', value: '70' },{ label: '71', value: '71' },{ label: '72', value: '72' }
  ];

  //下划线
  const underlineOptions = [
    { label: 'solid', value: 'solid' },
    { label: 'double', value: 'double' },
    { label: 'dashed', value: 'dashed' },
    { label: 'dotted', value: 'dotted' },
    { label: 'wavy', value: 'wavy' },
  ];
  
  //标题
  const titleOptions = [
    { label: '正文', value: null, fontSize: '16px' },
    { label: '标题1', value: 'first', fontSize: '26px' },
    { label: '标题2', value: 'second', fontSize: '24px' },
    { label: '标题3', value: 'third', fontSize: '22px' },
    { label: '标题4', value: 'fourth', fontSize: '20px' },
    { label: '标题5', value: 'fifth', fontSize: '18px' },
    { label: '标题6', value: 'sixth', fontSize: '16px' },
  ];
  
  //行间距
  const marginOptions = [
    { label: '1', value: '1' },
    { label: '1.25', value: '1.25' },
    { label: '1.5', value: '1.5' },
    { label: '1.75', value: '1.75' },
    { label: '2', value: '2' },
    { label: '2.5', value: '2.5' },
    { label: '3', value: '3' },
  ]
  
  //分割线
  const separatorOption = [
    { label: '0,0', value: '0,0' },
    { label: '1,1', value: '1,1' },
    { label: '3,1', value: '3,1' },
    { label: '4,4', value: '4,4' },
    { label: '7,3,3,3', value: '7,3,3,3' },
    { label: '6,2,2,2,2,2', value: '6,2,2,2,2,2' },
  ]
  
  //纸张大小
  const paperSizeOption = [
    { label: 'A4', value: '794*1123' },
    { label: 'A2', value: '1593*2251' },
    { label: 'A3', value: '1125*1593' },
    { label: 'A5', value: '565*796' },
    { label: '5号信封', value: '412*488' },
    { label: '6号信封', value: '450*866' },
    { label: '7号信封', value: '609*862' },
    { label: '9号信封', value: '862*1221' },
    { label: '法律用纸', value: '813*1266' },
    { label: '信纸', value: '813*1054' },
  ]
  
  //页面模式
  const pageModeOptions = [
    { label: '分页', value: 'paging' },
    { label: '连页', value: 'continuity' },
  ]  
  
  //纸张方向
  const paperDirectionOptions = [
    { label: '纵向', value: 'vertical' },
    { label: '横向', value: 'horizontal' },
  ]

  //加载状态
  const loading = ref<boolean>(false);
  let { formState, save, clearFormat, undo, redo, isApple, painterClick, painterDblClick, font, 
    fontSize, sizeAdd, sizeMinus, blob, itailc, underline, strikeout, superscript, subscript,
    color, pageMode, scaleAdd, scaleMiuns, paperSize, paperDirection, printTemplate, getPaperMargin,
    setPaperMargin, updateCatalog, setWordImage, setWordContent, leftAlign, centerAlign, rightAlign, alignmentAlign,
    rowMargin, pageBreak, cellColor, setHighlightColor, title, list, tabCell, separator, setHyperlink, getHyperlink,
    setWatermark, deleteWatermarkClick, arrowLeft, arrowRight, searchInput, searchKeyDown, replaceText, closeSearch } = method();
  //颜色的ref
  const colorRef = ref();
  initEditor();
  //字体option
  const fontOptionDom = ref<any>();
  //字体大小option
  const sizeOptionDom = ref<any>();
  //下划线option
  const underlineOptionDom = ref<any>();
  //页面模式option
  const pageModeOptionDom = ref<any>();
  //纸张大小option
  const paperSizeOptionDom = ref<any>();
  //纸张方向option
  const paperDirectionOptionDom = ref<any>();
  //行间距option
  const rowMarginOptionDom = ref<any>();
  //标题Option
  const titleOptionDom = ref<any>();
  //列表Option
  const listOptionDom = ref<any>();
  //table折叠层
  const tableCollapseDom = ref<any>();
  //分割线
  const separatorOptionDom = ref<any>();
  //水印
  const watermarkOptionDom = ref<any>();
  //查找替换
  const searchCollapseDom = ref<any>();
  //边距的Drawer
  const [registerPagerMarginDrawer, { openDrawer: openMarginDrawer }] = useDrawer();
  //超链接的Drawer
  const [registerHyperlinkDrawer, { openDrawer: openHyperlinkDrawer }] = useDrawer();
  //水印的Drawer
  const [registerWatermarkDrawer, { openDrawer: openHyperWatermarkDrawer }] = useDrawer();
  //上传图片的ref
  const uploadImageRef = ref();
  //上传文件的ref
  const uploadWordFileRef = ref();
  //单元格背景色ref
  const cellColorRef = ref();
  //单元格列的数组
  const tableCellList = ref<any>([]);
  //当前第几行
  const rowIndex = ref<number>(0);
  //当前第几列
  const colIndex = ref<number>(0);
  //弹窗model
  const [registerHighlightColorModal, { openModal: openHighlightColorModal }] = useModal();
  
  function initEditor() {
    setTimeout(() => {
      const fontDom: any = document.querySelector('.jeecg-menu-item-font');
      fontOptionDom.value = fontDom.querySelector('.options');
      //字体大小
      const jeecgSizeDom: any = document.querySelector('.jeecg-menu-item-size');
      sizeOptionDom.value = jeecgSizeDom.querySelector('.options');
      //下划线
      const underlineDom: any = document.querySelector('.jeecg-menu-item-underline');
      underlineOptionDom.value = underlineDom.querySelector('.options');   
      //分页
      const pageModeDom: any = document.querySelector('.page-mode');
      pageModeOptionDom.value = pageModeDom.querySelector('.options');
      //分页
      const pageSizeDom: any = document.querySelector('.paper-size');
      paperSizeOptionDom.value = pageSizeDom.querySelector('.options');
      //纸张方向
      const paperDirectionDom: any = document.querySelector('.paper-direction');
      paperDirectionOptionDom.value = paperDirectionDom.querySelector('.options'); 
      //行间距
      const rowMarginDom: any = document.querySelector('.jeecg-menu-item-row-margin');
      rowMarginOptionDom.value = rowMarginDom.querySelector('.options');
      //标题
      const titleDom: any = document.querySelector('.jeecg-menu-item-title');
      titleOptionDom.value = titleDom.querySelector('.options');
      //列表
      const listDom: any = document.querySelector('.jeecg-menu-item-list');
      listOptionDom.value = listDom.querySelector('.options'); 
      //表格
      tableCollapseDom.value = document.querySelector('.jeecg-menu-item-table-collapse');
      //分割线
      let separatorDom:any = document.querySelector('.jeecg-menu-item-separator');
      separatorOptionDom.value = separatorDom.querySelector('.options');
      //水印
      let watermarkDom:any = document.querySelector('.jeecg-menu-item-watermark');
      watermarkOptionDom.value = watermarkDom.querySelector('.options');
      //查找替换
      searchCollapseDom.value = document.querySelector('.jeecg-menu-item-search-collapse');
      
      setTableStyle()
      createEditor(props.content);
      // 组件挂载后添加事件监听器
      document.addEventListener('click', handleClickOutside);
      //监听Ctrl或Cmd键和S键事件
      document.addEventListener('keydown', handleKeyDown);
    }, 300);
  }

  /**
   * 下拉框点击事件
   */
  function fontFamilyClick(parentClass, childrenClass) {
    const font: any = document.querySelector(parentClass);
    const fontOption = font.querySelector(childrenClass);
    if (parentClass === '.jeecg-menu-item-font') {
      hiddenVisible('font');
    } else if (parentClass === '.jeecg-menu-item-size') {
      hiddenVisible('size');
    } else if (parentClass === '.jeecg-menu-item-underline') {
      hiddenVisible('underline');
    } else if(parentClass === '.page-mode'){
      hiddenVisible('pageMode');
    } else if(parentClass === '.paper-size'){
      hiddenVisible('paperSize');
    } else if(parentClass === '.paper-direction'){
      hiddenVisible('paperDirection');
    } else if(parentClass === '.jeecg-menu-item-row-margin'){
      hiddenVisible('rowMargin');
    }else if(parentClass === '.jeecg-menu-item-title'){
      hiddenVisible('title');
    }else if(parentClass === '.jeecg-menu-item-list'){
      hiddenVisible('list');
    }else if(parentClass === '.jeecg-menu-item-separator'){
      hiddenVisible('separator');
    }else if(parentClass === '.jeecg-menu-item-watermark'){
      hiddenVisible('watermark');
    }
    fontOption.classList.toggle('visible');
    setDropdownContentPosition(font, fontOption);
  }

  /**
   * 动态设置展开面板的位置
   * @param parentNode
   * @param dropdownNode
   */
  function setDropdownContentPosition(parentNode, dropdownNode) {
    const dropdownContent = dropdownNode;
    const rect = parentNode.getBoundingClientRect();
    const screenWidth = window.innerWidth;
    dropdownContent.style.top = `${rect.bottom}px`;
    dropdownContent.style.left = `${rect.left}px`;
    if (rect.left + dropdownContent.offsetWidth > screenWidth) {
      dropdownContent.style.left = `${rect.right - dropdownContent.offsetWidth}px`;
    }
  }

  /**
   * 监听关闭事件
   * @param event
   */
  const handleClickOutside = (event) => {
    if (!event.target.closest('.select') && !event.target.closest(".page-mode-i")) {
      hiddenVisible();
    }
  };

  /**
   * 颜色点击事件
   */
  function colorClick() {
    colorRef.value.click();
  }

  /**
   * 导出模板
   */
  function downloadTemplate() {
    emit('download', props.content);
  }

  /**
   * 保存
   */
  function saveTemplate() {
    let saveData = save();
    const params = Object.assign(props.content, saveData);
    emit('save', params);
  }

  /**
   * 隐藏下拉框的显示
   * 
   * @param value
   */
  function hiddenVisible(value = ''){
      if(!value || value !== 'font'){
        fontOptionDom.value.classList.remove('visible');
      }
      if(!value || value !== 'size'){
        sizeOptionDom.value.classList.remove('visible');
      }
      if(!value || value !== 'underline'){
        underlineOptionDom.value.classList.remove('visible');
      }
      if(!value || value !== 'pageMode'){
        pageModeOptionDom.value.classList.remove('visible');
      }   
      if(!value || value !== 'paperSize'){
        paperSizeOptionDom.value.classList.remove('visible');
      }
      if(!value || value !== 'paperDirection'){
        paperDirectionOptionDom.value.classList.remove('visible');
      }
      if(!value || value !== 'rowMargin'){
        rowMarginOptionDom.value.classList.remove('visible');
      }  
      if(!value || value !== 'title'){
        titleOptionDom.value.classList.remove('visible');
      } 
      if(!value || value !== 'list'){
        listOptionDom.value.classList.remove('visible');
      }
      if(!value || value !== 'table'){
        recoveryTable();
      } 
      if(!value || value !== 'separator'){
        separatorOptionDom.value.classList.remove('visible');
      } 
      if(!value || value !== 'watermark'){
        watermarkOptionDom.value.classList.remove('visible');
      }
  }

  /**
   * 边距点击事件
   */
  function paperMarginClick() {
    let values = getPaperMargin();
    openMarginDrawer(true,{
      ...values
    })
  }

  /**
   * 打开目录
   */
  function openDirectory() {
    const catalogDom:any = document.querySelector('.catalog');
    catalogDom.style.display = 'block';
    formState.showCataLogDom = true;
    updateCatalog();
  }
  
  /**
   * 关闭目录
   */
  function closeDirectory() {
    const catalogDom:any = document.querySelector('.catalog');
    formState.showCataLogDom = false;
    catalogDom.style.display = 'none';
  }
  
  /**
   * 图片上传
   */
  function uploadImage(){
    uploadImageRef.value.click();
  }
  //图片上传成功事件
  const uploadImageSuccess = (fileInfo) => {
    if(fileInfo.success){
      let fileUrl = getFileAccessHttpUrl(fileInfo.message);
      setWordImage(fileUrl);
    }
  }
  
  /**
   * 文件上传改变事件
   * @param e
   */
  function uploadImageChange(e) {
    let file = e.target.files[0];
    defHttp.uploadFile({ url: props.uploadImageUrl },  { file: file, filename: file.name, data: { biz: 'word' } }, { success: uploadImageSuccess });
  }

  //文件上传成功事件
  const uploadFileSuccess = (fileInfo) => {
    if(fileInfo.success){
      setWordContent(fileInfo.result);
    }
  }
  
  /**
   * 上传文件
   */
  function uploadWordFile() {
    uploadWordFileRef.value.click();
  }

  /**
   * 上传文件change
   * @param e
   */
  function uploadDocxChange(e) {
    let file = e.target.files[0];
    defHttp.uploadFile({ url: props.uploadFileUrl },  { file: file, filename: file.name }, { success: uploadFileSuccess });
  }

  /**
   * 单元格背景色点击事件
   */
  function cellColorClick() {
    cellColorRef.value.click();
  }

  /**
   * 高亮颜色点击事件爱你
   */
  function highLightColorClick() {
    openHighlightColorModal(true,{
      highlightColor:formState.highlightColor
    })
  }

  /**
   * 设置table9*9样式
   */
  function setTableStyle() {
    const tablePanel:any = document.querySelector('.table-panel');
    // 绘制行列
    tableCellList.value = []
    for (let i = 0; i < 10; i++) {
      const tr = document.createElement('tr');
      tr.classList.add('table-row');
      const trCellList = []
      for (let j = 0; j < 10; j++) {
        const td = document.createElement('td');
        td.classList.add('table-cel');
        tr.append(td);
        trCellList.push(td);
      }
      tablePanel.append(tr);
      tableCellList.value.push(trCellList);
    }
  }
  /**
   * 表格点击事件
   */
  function tableClick() {
    hiddenVisible('table');
    const tableDom = document.querySelector('.jeecg-menu-item-table');
    tableCollapseDom.value.style.display = 'block';
    setDropdownContentPosition(tableDom, tableCollapseDom.value);
  }

  /**
   * 移除所有格选择
   */
  function removeAllTableCellSelect() {
    tableCellList.value.forEach(tr => {
      tr.forEach(td => td.classList.remove('active'))
    })
  }

  /**
   * 设置标题内容
   * @param payload
   */
  function setTableTitle(payload) {
    const tableTitle:any = document.querySelector('.table-select');
    tableTitle.innerText = payload;
  }

  /**
   * 恢复初始状态
   */
  function recoveryTable() {
    // 还原选择样式、标题、选择行列
    removeAllTableCellSelect()
    setTableTitle('插入')
    colIndex.value = 0
    rowIndex.value = 0
    // 隐藏panel
    tableCollapseDom.value.style.display = 'none';
  }

  /**
   * 表格鼠标移动事件
   * @param evt
   */
  function tableMousemove(evt) {
    const celSize = 16;
    const rowMarginTop = 10;
    const celMarginRight = 6;
    const { offsetX, offsetY } = evt;
    // 移除所有选择
    removeAllTableCellSelect()
    colIndex.value = Math.ceil(offsetX / (celSize + celMarginRight)) || 1;
    rowIndex.value = Math.ceil(offsetY / (celSize + rowMarginTop)) || 1;
    // 改变选择样式
    tableCellList.value.forEach((tr, trIndex) => {
      tr.forEach((td, tdIndex) => {
        if (tdIndex < colIndex.value && trIndex < rowIndex.value) {
          td.classList.add('active');
        }
      })
    })
    // 改变表格标题
    setTableTitle(`${rowIndex.value}×${colIndex.value}`);
  }

  /**
   * table单元格单击事件
   */
  function tableCellClick() {
    tabCell(rowIndex.value,colIndex.value,recoveryTable);
  }

  /**
   * 超链接点击事件
   */
  function hyperlinkClick() {
    let hyperlink = getHyperlink();
    openHyperlinkDrawer(true,{
      ...hyperlink
    })
  }

  /**
   * 添加水印
   */
  function addWatermarkClick(){
    openHyperWatermarkDrawer(true,{});
  }

  /**
   * 查找点击事件
   */
  function searchClick() {
    searchCollapseDom.value.style.display = 'block';
    const searchDom:any =document.querySelector('.jeecg-menu-item-search');
    const searchInputDom:any = document.querySelector('.jeecg-menu-item-search-collapse-search input')
    const searchCollapseRect = searchCollapseDom.value.getBoundingClientRect();
    const bodyRect = document.body.getBoundingClientRect();
    const searchRect = searchDom.getBoundingClientRect();
    if (searchRect.left + searchCollapseRect.width > bodyRect.width) {
      searchCollapseDom.value.style.right = '0px';
      searchCollapseDom.value.style.left = 'unset';
    } else {
      searchCollapseDom.value.style.right = 'unset';
      searchCollapseDom.value.style.left = searchRect.left + 'px';
    }
    searchInputDom.focus();
  }


  /**
   * 检查是否按下了Ctrl或Cmd键和S键
   * @param event
   */
  function handleKeyDown(event) {
    // 检查是否按下了Ctrl或Cmd键和S键
    if ((event.ctrlKey || event.metaKey) && event.key === 's') {
      event.preventDefault();
      //保存
      saveTemplate();
    }
  }

  //是否显示输入框
  const showInput = ref<boolean>(false);
  //标题输入框
  const nameRef = ref();
  
  /**
   * 显示或隐藏输入框
   */
  function showInputClick() {
    showInput.value = true;
    nextTick(()=>{
      nameRef.value.focus();
      nameRef.value.select();
    })
  }

  /**
   * 保存模板名称事件
   */
  function saveTemplateNameClick() {
    showInput.value = false;
    let saveData = save();
    if(formState.name != props.content.name){
      const params = Object.assign(props.content, saveData, { name: formState.name });
      emit('save', params);
    }
  }

  /**
   * 分隔符点击事件
   */
  function separatorClick() {
    hiddenVisible('separator');
    const separator: any = document.querySelector(".jeecg-menu-item-separator");
    const separatorOption = separator.querySelector(".options");
    separatorOption.classList.add('visible');
    setDropdownContentPosition(separator, separatorOption);
  }

  /**
   * 分割线确定事件
   */
  function separatorOk() {
    const separators: any = document.querySelector(".jeecg-menu-item-separator");
    const separatorOption = separators.querySelector(".options");
    separatorOption.classList.remove('visible');
    console.log("我进来了1111")
    separator();
  }
  
  /**
   * 分割线取消事件
   */
  function separatorCancel() {
    separatorOptionDom.value.classList.remove('visible');
    formState.separatorColor = '#000000';
    formState.separatorLineWidth = 1;
    formState.separator = '0,0';
    separatorOptionDom.value.querySelectorAll('li') .forEach(li => li.classList.remove('active'));
  }

  /**
   * 分割线下拉选中事件
   * @param value
   */
  function separatorActiveClick(value) {
    separatorOptionDom.value.querySelectorAll('li') .forEach(li => li.classList.remove('active'));
    const curSeparatorDom:any = separatorOptionDom.value.querySelector(`[data-separator='${value}']`);
    if (curSeparatorDom) {
      formState.separator = value;
      curSeparatorDom.classList.add('active');
    }
  }
  
  
  onUnmounted(() => {
    // 组件卸载前移除事件监听器，防止内存泄漏
    document.removeEventListener('click', handleClickOutside);
    // 移出键盘事件
    document.removeEventListener('keydown', handleKeyDown);
  });
</script>

<style scoped lang="scss" src="./style/DocDesign.css"></style>
