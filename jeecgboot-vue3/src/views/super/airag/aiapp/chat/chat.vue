<template>
  <div class="chatWrap">
    <div class="content">
      <div class="header-title" v-if="type === 'view' && headerTitle">
        <div class="title-content">
          <span>{{headerTitle}}</span>
          <a-button 
            v-if="hasExtraFlowInputs" 
            type="text" 
            class="edit-btn" 
            @click="handleEditSettings"
            title="参数设置"
          >
            <Icon icon="ant-design:setting-outlined" :size="16" />
          </a-button>
        </div>
        <div class="header-actions">
          <div v-if="showAdvertising" class="header-advertisint">
            AI客服由
            <a style="color: #4183c4;margin-left: 2px;margin-right: 2px" href="https://jeecg.com/aigcIndex" target="_blank">
              JEECG AI
            </a>
            提供
          </div>
        </div>
      </div>
      <div class="main">
        <div id="scrollRef" ref="scrollRef" class="scrollArea">
          <template v-if="chatData.length>0">
            <div class="chatContentArea">
              <chatMessage
                v-for="(item, index) of chatData"
                :key="index"
                :date-time="item.dateTime || item.datetime"
                :text="item.content"
                :inversion="item.inversion || item.role"
                :error="item.error"
                :errorMsg="item.errorMsg"
                :currentToolTag="currentToolTag"
                :loading="item.loading"
                :appData="appData"
                :presetQuestion="item.presetQuestion"
                :images = "item.images"
                :files = "item.files"
                :retrievalText="item.retrievalText"
                :referenceKnowledge="item.referenceKnowledge"
                :eventType="item.eventType"
                :showAvatar="item.showAvatar"
                :isLast="index === chatData.length -1"
                @send="handleOutQuestion"
              ></chatMessage>
            </div>
          </template>
        </div>
      </div>
      <div class="footer">
        <div class="topArea">
          <presetQuestion @out-question="handleOutQuestion" :quickCommandData="quickCommandData"></presetQuestion>
        </div>
        <div class="bottomArea">
          <a-button type="text" class="delBtn" @click="handleDelSession()">
            <svg
              t="1706504908534"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="1584"
              width="18"
              height="18"
            >
              <path
                d="M816.872727 158.254545h-181.527272V139.636364c0-39.563636-30.254545-69.818182-69.818182-69.818182h-107.054546c-39.563636 0-69.818182 30.254545-69.818182 69.818182v18.618181H207.127273c-48.872727 0-90.763636 41.890909-90.763637 93.09091s41.890909 90.763636 90.763637 90.763636h609.745454c51.2 0 90.763636-41.890909 90.763637-90.763636 0-51.2-41.890909-93.090909-90.763637-93.09091zM435.2 139.636364c0-13.963636 9.309091-23.272727 23.272727-23.272728h107.054546c13.963636 0 23.272727 9.309091 23.272727 23.272728v18.618181h-153.6V139.636364z m381.672727 155.927272H207.127273c-25.6 0-44.218182-20.945455-44.218182-44.218181 0-25.6 20.945455-44.218182 44.218182-44.218182h609.745454c25.6 0 44.218182 20.945455 44.218182 44.218182 0 23.272727-20.945455 44.218182-44.218182 44.218181zM835.490909 407.272727h-121.018182c-13.963636 0-23.272727 9.309091-23.272727 23.272728s9.309091 23.272727 23.272727 23.272727h97.745455V837.818182c0 39.563636-30.254545 69.818182-69.818182 69.818182h-37.236364V602.763636c0-13.963636-9.309091-23.272727-23.272727-23.272727s-23.272727 9.309091-23.272727 23.272727V907.636364h-118.690909V602.763636c0-13.963636-9.309091-23.272727-23.272728-23.272727s-23.272727 9.309091-23.272727 23.272727V907.636364H372.363636V602.763636c0-13.963636-9.309091-23.272727-23.272727-23.272727s-23.272727 9.309091-23.272727 23.272727V907.636364h-34.909091c-39.563636 0-69.818182-30.254545-69.818182-69.818182V453.818182H558.545455c13.963636 0 23.272727-9.309091 23.272727-23.272727s-9.309091-23.272727-23.272727-23.272728H197.818182c-13.963636 0-23.272727 9.309091-23.272727 23.272728V837.818182c0 65.163636 51.2 116.363636 116.363636 116.363636h451.490909c65.163636 0 116.363636-51.2 116.363636-116.363636V430.545455c0-13.963636-11.636364-23.272727-23.272727-23.272728z"
                fill="currentColor"
                p-id="1585"
              />
            </svg>
          </a-button>
          <a-button v-if="type === 'view'" type="text" class="contextBtn" :class="[usingContext && 'enabled']" @click="handleUsingContext">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              aria-hidden="true"
              role="img"
              class="iconify iconify--ri"
              width="20"
              height="20"
              viewBox="0 0 24 24"
            >
              <path
                fill="currentColor"
                d="M12 2c5.523 0 10 4.477 10 10s-4.477 10-10 10a9.956 9.956 0 0 1-4.708-1.175L2 22l1.176-5.29A9.956 9.956 0 0 1 2 12C2 6.477 6.477 2 12 2m0 2a8 8 0 0 0-8 8c0 1.335.326 2.618.94 3.766l.35.654l-.656 2.946l2.948-.654l.653.349A7.955 7.955 0 0 0 12 20a8 8 0 1 0 0-16m1 3v5h4v2h-6V7z"
              />
            </svg>
          </a-button>
          <div class="chat-textarea" :class="textareaActive?'textarea-active':''">
            <!--   begin 兼容文件显示   -->
            <div class="textarea-top" v-if="(uploadUrlList && uploadUrlList.length>0) || (fileList && fileList.length>0)">
              <!-- 只拥有图片 -->
              <template v-if="(!fileList || fileList.length===0)">
                <div v-for="(item,index) in uploadUrlList" class="top-image" :key="index">
                  <img :src="getImage(item)" @click="handlePreview(item)"/>
                  <div class="upload-icon" @click="deleteImage(index)">
                    <Icon icon="ant-design:close-outlined" size="12px"></Icon>
                  </div>
                </div>
              </template>
              <!-- 拥有文件 -->
              <template v-else>
                <div class="file-card-container" style="display: flex; gap: 8px; flex-wrap: wrap;">
                  <!-- 图片渲染 -->
                  <div v-for="(url, index) in uploadUrlList" :key="'img-'+index" class="file-card">
                    <div class="file-card-icon">
                      <img :src="getImage(url)" class="file-thumb" @click="handlePreview(url)"/>
                    </div>
                    <div class="file-card-info">
                      <div class="file-name" :title="fileInfoList[index]?.name || '图片'">{{ fileInfoList[index]?.name || '图片' }}</div>
                      <div class="file-size">{{ calculateFileSize(fileInfoList[index]?.size) }}</div>
                    </div>
                    <div class="file-card-close" @click="deleteImage(index)">
                      <Icon icon="ant-design:close-outlined" size="12px"/>
                    </div>
                  </div>
                  <!-- 文件渲染 -->
                  <template v-for="(item, index) in fileList" :key="'file-'+index">
                    <div class="file-card" v-if="item.status !== 'error'">
                      <div class="file-card-icon">
                        <Icon :icon="getFileIcon(item.name)" :color="getFileIconColor(item.name)" size="32" />
                      </div>
                      <div class="file-card-info">
                        <div class="file-name" :title="item.name">{{ item.name }}</div>
                        <div class="file-size">{{ calculateFileSize(item.size) }}</div>
                      </div>
                      <div class="file-card-close" @click="deleteFile(index)">
                        <Icon icon="ant-design:close-outlined" size="12px"/>
                      </div>
                    </div>
                  </template>
                </div>
              </template>
              <!--   end 兼容文件显示   -->
            </div>
            <div class="textarea-bottom">
              <a-textarea
                  ref="inputRef"
                  v-model:value="prompt"
                  :autoSize="{ minRows: 1, maxRows: 6 }"
                  :placeholder="placeholder"
                  @press-enter="handleEnter"
                  @focus="textareaActive = true"
                  @blur="textareaActive = false"
                  autofocus
                  :readonly="loading"
                  style="border-color: #ffffff !important;box-shadow:none"
                  @paste="paste"
              >
              </a-textarea>
              <div class="textarea-action-bar">
                <div class="left-actions">
                <a-dropdown placement="topLeft" trigger="['click']" overlayClassName="chat-upload-dropdown">
                  <template #overlay>
                    <a-menu mode="vertical">
                      <a-menu-item key="img">
                        <a-upload
                            accept=".jpg,.jpeg,.png"
                            v-if="!loading"
                            name="file"
                            v-model:file-list="fileInfoList"
                            :showUploadList="false"
                            :headers="headers"
                            :beforeUpload="beforeUpload"
                            @change="handleChange"
                            :multiple="true"
                            :action="uploadUrl"
                            :max-count="3"
                        >
                          <div style="display: flex; align-items: center">
                            <Icon icon="ant-design:picture-outlined" style="margin-right:8px;color:#3d4353" />
                            上传图片
                          </div>
                        </a-upload>
                      </a-menu-item>
                      <a-menu-item key="file">
                        <a-upload
                            accept=".txt, .pdf, .docx, .doc, .pptx, .ppt, .xlsx, .xls, .md"
                            :maxCount="3"
                            v-if="!loading"
                            name="file"
                            v-model:file-list="fileList"
                            :showUploadList="false"
                            :headers="headers"
                            :beforeUpload="beforeUploadFile"
                            @change="handleChangeFile"
                            :multiple="true"
                            :action="uploadUrl"
                        >
                          <div style="display: flex; align-items: center">
                            <Icon icon="ant-design:file-add-outlined" style="margin-right:8px;color:#3d4353" />
                            上传文件
                          </div>
                        </a-upload>
                      </a-menu-item>
                    </a-menu>
                  </template>
                  <a-button class="sendBtn" type="text">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><path d="M17.3977 3.9588C15.8361 2.39727 13.3037 2.39727 11.7422 3.9588L5.03365 10.6673C2.60612 13.0952 2.60612 17.0314 5.03365 19.4592C7.46144 21.887 11.3983 21.8875 13.8262 19.4599L20.5348 12.7514C20.8472 12.439 21.3534 12.439 21.6658 12.7514C21.9781 13.0638 21.9782 13.5701 21.6658 13.8825L14.9573 20.591C11.9046 23.6435 6.95518 23.6429 3.90255 20.5903C0.850191 17.5377 0.850191 12.5889 3.90255 9.53624L10.6111 2.82771C12.7975 0.641334 16.3424 0.641334 18.5288 2.82771C20.7149 5.01409 20.7151 8.55906 18.5288 10.7454L11.8699 17.4042C10.5369 18.7372 8.37542 18.7365 7.04241 17.4035C5.70963 16.0705 5.7095 13.9096 7.04241 12.5767L13.7012 5.91785C14.0136 5.60547 14.5199 5.60557 14.8323 5.91785C15.1447 6.23027 15.1447 6.73652 14.8323 7.04894L8.1735 13.7078C7.46543 14.4159 7.46556 15.5642 8.1735 16.2724C8.88167 16.9806 10.03 16.9806 10.7381 16.2724L17.397 9.61358C18.9584 8.05211 18.959 5.52035 17.3977 3.9588Z" fill="currentColor"></path></svg>
                  </a-button>
                </a-dropdown>
                  <a-divider type="vertical" v-if="showThink || showWebSearch || showDraw "/>
                  <a-tooltip v-if="showThink" :title="enableThink ? '关闭深度思考' : '开启深度思考'">
                    <a-button
                        class="sendBtn webSearchBtn"
                        type="text"
                        :class="{ 'enabled': enableThink }"
                        @click="toggleThink">
                      <svg style="margin-right: 6px" :style="enableThink ? { color: '#06f' } : { color: '#3d4353' }" width="16" height="16" viewBox="0 0 14 14" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M7.06428 5.93342C7.6876 5.93342 8.19304 6.43904 8.19319 7.06233C8.19319 7.68573 7.68769 8.19123 7.06428 8.19123C6.44096 8.19113 5.93537 7.68567 5.93537 7.06233C5.93552 6.43911 6.44105 5.93353 7.06428 5.93342Z" fill="currentColor"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M8.68147 0.963693C10.1168 0.447019 11.6266 0.374829 12.5633 1.31135C13.5 2.24805 13.4276 3.75776 12.911 5.19319C12.7126 5.74431 12.4385 6.31796 12.0965 6.89729C12.4969 7.54638 12.8141 8.19018 13.036 8.80647C13.5527 10.2419 13.625 11.7516 12.6883 12.6883C11.7516 13.625 10.2419 13.5527 8.80647 13.036C8.19019 12.8141 7.54638 12.4969 6.89729 12.0965C6.31794 12.4386 5.74432 12.7125 5.19319 12.911C3.75774 13.4276 2.24807 13.5 1.31135 12.5633C0.374829 11.6266 0.447019 10.1168 0.963693 8.68147C1.17182 8.10338 1.46318 7.50063 1.82893 6.8924C1.52179 6.35711 1.27232 5.82825 1.08869 5.31819C0.572038 3.88278 0.499683 2.37306 1.43635 1.43635C2.37304 0.499655 3.88277 0.572044 5.31819 1.08869C5.82825 1.27232 6.35712 1.5218 6.8924 1.82893C7.50063 1.46318 8.10338 1.17181 8.68147 0.963693ZM11.3572 8.01154C10.9083 8.62253 10.3901 9.22873 9.8094 9.8094C9.22874 10.3901 8.62252 10.9083 8.01154 11.3572C8.42567 11.5841 8.82867 11.7688 9.21272 11.9071C10.5455 12.3868 11.4246 12.2547 11.8397 11.8397C12.2547 11.4246 12.3869 10.5456 11.9071 9.21272C11.7688 8.82866 11.5841 8.42568 11.3572 8.01154ZM2.56526 8.02912C2.3734 8.39322 2.21492 8.74796 2.0926 9.08772C1.61288 10.4204 1.74509 11.2995 2.15998 11.7147C2.57502 12.1297 3.45412 12.2618 4.78694 11.7821C5.11053 11.6656 5.44783 11.5164 5.79377 11.3367C5.24897 10.9223 4.70919 10.4533 4.19026 9.9344C3.57575 9.31987 3.03166 8.67633 2.56526 8.02912ZM6.90705 3.2469C6.24062 3.70479 5.56457 4.26321 4.91389 4.91389C4.26322 5.56456 3.70479 6.24063 3.2469 6.90705C3.72671 7.63325 4.32774 8.37685 4.91389 8.96299C5.50003 9.54914 6.24362 10.1502 6.96983 10.6299C7.69601 10.1502 8.43961 9.54914 9.02575 8.96299C9.6119 8.37685 10.2129 7.63325 10.6927 6.90705C10.2129 6.18086 9.6119 5.43725 9.02575 4.8511C8.43961 4.26496 7.69601 3.66391 6.96983 3.18419C6.94896 3.205 6.92803 3.22593 6.90705 3.2469Z" fill="currentColor"></path></svg>
                      深度思考
                    </a-button>
                  </a-tooltip>
                  <a-tooltip v-if="showWebSearch" :title="enableSearch ? '关闭联网搜索' : '开启联网搜索'">
                    <a-button
                        class="sendBtn webSearchBtn"
                        type="text"
                        :class="{ 'enabled': enableSearch }"
                        @click="toggleWebSearch">
                      <Icon size="16" icon="ant-design:global-outlined" :style="enableSearch ? { color: '#06f' } : { color: '#3d4353' }"></Icon>
                      联网搜索
                    </a-button>
                  </a-tooltip>
                  <a-tooltip v-if="showDraw" :title="enableDraw ? '关闭图像生成' : '开启图像生成'">
                    <a-button
                        class="sendBtn webSearchBtn"
                        type="text"
                        :class="{ 'enabled': enableDraw }"
                        @click="handleGenerateImage">
                      <svg style="margin-right: 6px" :style="enableDraw ? { color: '#06f' } : { color: '#3d4353' }" width="16" height="16" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg" class=""><path d="M12 2.3584C14.1681 2.35841 16.1541 2.52965 17.7266 2.72754C19.9228 3.00409 21.6336 4.66074 21.9365 6.85352C22.1348 8.28975 22.2998 10.0677 22.2998 12L22.293 12.7168C22.2586 14.3712 22.1101 15.8897 21.9365 17.1465L21.9043 17.3496C21.5268 19.4411 19.8545 21.0045 17.7266 21.2725L17.1182 21.3457C15.655 21.511 13.8972 21.6416 12 21.6416L11.1963 21.6338C9.60724 21.6034 8.13686 21.4874 6.88281 21.3457L6.27441 21.2725C4.14635 21.0046 2.47428 19.4411 2.09668 17.3496L2.06445 17.1465C1.89093 15.8897 1.74239 14.3712 1.70801 12.7168L1.7002 12C1.7002 10.3092 1.82669 8.737 1.99121 7.4082L2.06445 6.85352C2.35801 4.72923 3.9719 3.10743 6.06934 2.75684L6.27441 2.72754C7.84674 2.52969 9.83219 2.35841 12 2.3584ZM11.9775 13.3496C11.4613 13.3496 10.9378 13.4818 10.2207 13.8066C9.48747 14.1388 8.61112 14.6435 7.37793 15.3555L3.76367 17.4424C4.13152 18.6436 5.16153 19.5204 6.47363 19.6855C7.99607 19.8771 9.91342 20.042 12 20.042C14.0865 20.042 16.0039 19.8771 17.5264 19.6855C18.8303 19.5214 19.8566 18.6546 20.2305 17.4648L16.5771 15.3555C15.344 14.6435 14.4676 14.1388 13.7344 13.8066C13.0173 13.4818 12.4938 13.3496 11.9775 13.3496ZM12 3.95801C9.91342 3.95802 7.99607 4.12286 6.47363 4.31445C4.98011 4.50243 3.85117 5.61215 3.64941 7.07324C3.45876 8.45412 3.2998 10.1566 3.2998 12C3.2998 13.3468 3.38385 14.6183 3.50391 15.7441L6.57715 13.9707C7.78367 13.2741 8.73894 12.7218 9.56055 12.3496C10.3981 11.9702 11.1542 11.75 11.9775 11.75C12.8008 11.75 13.557 11.9702 14.3945 12.3496C15.2161 12.7218 16.1714 13.2741 17.3779 13.9707L20.4922 15.7686C20.6134 14.6367 20.7002 13.3565 20.7002 12C20.7002 10.1566 20.5422 8.4541 20.3516 7.07324C20.1498 5.61218 19.0198 4.50249 17.5264 4.31445C16.0039 4.12287 14.0865 3.95802 12 3.95801ZM7.73438 7.0625C8.76128 7.0625 9.59375 7.89497 9.59375 8.92188C9.59375 9.94878 8.76128 10.7812 7.73438 10.7812C6.70747 10.7812 5.875 9.94878 5.875 8.92188C5.875 7.89497 6.70747 7.0625 7.73438 7.0625Z" fill="currentColor"></path></svg>
                      <span style="font-size: 14px">图像生成</span>
                    </a-button>
                  </a-tooltip>
                </div>
                <div class="right-actions">
                <a-button v-if="loading" type="primary" danger @click="handleStopChat" class="stopBtn">
                  <svg
                      t="1706148514627"
                      class="icon"
                      viewBox="0 0 1024 1024"
                      version="1.1"
                      xmlns="http://www.w3.org/2000/svg"
                      p-id="5214"
                      width="18"
                      height="18"
                  >
                    <path
                        d="M512 967.111111c-250.311111 0-455.111111-204.8-455.111111-455.111111s204.8-455.111111 455.111111-455.111111 455.111111 204.8 455.111111 455.111111-204.8 455.111111-455.111111 455.111111z m0-56.888889c221.866667 0 398.222222-176.355556 398.222222-398.222222s-176.355556-398.222222-398.222222-398.222222-398.222222 176.355556-398.222222 398.222222 176.355556 398.222222 398.222222 398.222222z"
                        fill="currentColor"
                        p-id="5215"
                    />
                    <path d="M341.333333 341.333333h341.333334v341.333334H341.333333z" fill="currentColor" p-id="5216"/>
                  </svg>
                </a-button>
                <a-button
                    @click="
              () => {
                handleSubmit();
              }
            "
                    :disabled="!prompt"
                    class="sendBtn"
                    type="text"
                    v-if="!loading"
                >
                  <svg
                      t="1706147858151"
                      class="icon"
                      viewBox="0 0 1024 1024"
                      version="1.1"
                      xmlns="http://www.w3.org/2000/svg"
                      p-id="4237"
                      width="18"
                      height="18"
                  >
                    <path
                        d="M865.28 202.5472c-17.1008-15.2576-41.0624-19.6608-62.5664-11.5712L177.7664 427.1104c-23.2448 8.8064-38.5024 29.696-39.6288 54.5792-1.1264 24.8832 11.9808 47.104 34.4064 58.0608l97.5872 47.7184c4.5056 2.2528 8.0896 6.0416 9.9328 10.6496l65.4336 161.1776c7.7824 19.1488 24.4736 32.9728 44.7488 37.0688 20.2752 4.096 41.0624-2.1504 55.6032-16.7936l36.352-36.352c6.4512-6.4512 16.5888-7.8848 24.576-3.3792l156.5696 88.8832c9.4208 5.3248 19.8656 8.0896 30.3104 8.0896 8.192 0 16.4864-1.6384 24.2688-5.0176 17.8176-7.68 30.72-22.8352 35.4304-41.6768l130.7648-527.1552c5.5296-22.016-1.7408-45.2608-18.8416-60.416z m-20.8896 50.7904L713.5232 780.4928c-1.536 6.2464-5.8368 11.3664-11.776 13.9264s-12.5952 2.1504-18.2272-1.024L526.9504 704.512c-9.4208-5.3248-19.8656-7.9872-30.208-7.9872-15.9744 0-31.744 6.144-43.52 17.92l-36.352 36.352c-3.8912 3.8912-8.9088 5.9392-14.2336 6.0416l55.6032-152.1664c0.512-1.3312 1.2288-2.56 2.2528-3.6864l240.3328-246.1696c8.2944-8.4992-2.048-21.9136-12.3904-16.0768L301.6704 559.8208c-4.096-3.584-8.704-6.656-13.6192-9.1136L190.464 502.9888c-11.264-5.5296-11.5712-16.1792-11.4688-19.3536 0.1024-3.1744 1.536-13.824 13.2096-18.2272L817.152 229.2736c10.4448-3.9936 18.0224 1.3312 20.8896 3.8912 2.8672 2.4576 9.0112 9.3184 6.3488 20.1728z"
                        p-id="4238"
                        fill="currentColor"
                    />
                  </svg>
                </a-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { Ref, watch } from 'vue';
  import { computed, ref, createVNode, onUnmounted, onMounted, nextTick } from 'vue';
  import { useScroll } from './js/useScroll';
  import chatMessage from './chatMessage.vue';
  import presetQuestion from './presetQuestion.vue';
  import { DeleteOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { message, Modal, Tabs } from 'ant-design-vue';
  import './style/github-markdown.less';
  import './style/highlight.less';
  import './style/github-markdown.less';
  import dayjs from 'dayjs';
  import { defHttp } from '@/utils/http/axios';
  import { cloneDeep } from "lodash-es";
  import { calculateFileSize, getFileAccessHttpUrl, getFileIcon, getHeaders, getFileIconColor } from "@/utils/common/compUtils";
  import { createImgPreview } from "@/components/Preview";
  import { useAppInject } from "@/hooks/web/useAppInject";
  import { useGlobSetting } from "@/hooks/setting";
  import { Icon } from '/@/components/Icon';

  message.config({
    prefixCls: 'ai-chat-message',
  });

  const props = defineProps(['uuid', 'prologue', 'formState', 'url', 'type','historyData','chatTitle','presetQuestion','quickCommandData','showAdvertising','hasExtraFlowInputs','conversationSettings','sessionType']);
  const emit = defineEmits(['save','reload-message-title','edit-settings']);
  const { scrollRef, scrollToBottom } = useScroll();
  const prompt = ref<string>('');
  const loading = ref<boolean>(false);
  const inputRef = ref<Ref | null>(null);
  const headerTitle = ref<string>(props.chatTitle);

  //聊天数据
  const chatData = ref<any>([]);
  //应用数据
  const appData = ref<any>({});
  const usingContext = ref<any>(true);
  const uuid = ref<string>(props.uuid);
  const topicId = ref<string>('');
  //请求id
  const requestId = ref<string>('');
  const { getIsMobile } = useAppInject();
  const conversationList = computed(() => chatData.value.filter((item) => item.inversion != 'user' && !!item.conversationOptions));
  const placeholder = computed(() => {
    if(getIsMobile.value){
      return '来说点什么吧...'
    } else {
      return '来说点什么吧...（Shift + Enter = 换行）';
    }
  });
  //token
  const headers = getHeaders();
  //文本域点击事件
  const textareaActive = ref<boolean>(false);

  const globSetting = useGlobSetting();
  const baseUploadUrl = globSetting.uploadUrl;
  const uploadUrl = ref<string>(`${baseUploadUrl}/airag/chat/upload`);
  //是否为断线重连
  const isReConnect = ref<boolean>(false);
  //是否存在思考过程
  const isThinking = ref<boolean>(false);
  //是否开启网络搜索
  const enableSearch = ref<boolean>(false);
  //是否显示网络搜索按钮（只有千问模型支持）
  const showWebSearch = ref<boolean>(false);
  //模型provider信息
  const modelProvider = ref<string>('');
  //是否显示深度思考( 只有deepsee-reason支持 )
  const showThink = ref<boolean>(false);
  //是否开启深度思考
  const enableThink = ref<boolean>(false);
  //模型名称
  const modelName = ref<string>('');
  //是否开启绘画
  const enableDraw = ref<boolean>(false);
  //是否显示工具栏
  const showDraw = ref<boolean>(false);
  //绘画模型的id
  const drawModelId = ref<string>('');
  //其他文件列表
  const fileUrlList = ref<any>([]);
  //文件列表（用于显示和管理）
  const fileList = ref<any>([]);

  // 当前正在调用的工具
  const currentToolTag = ref<string>('');

  function handleEnter(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      handleSubmit();
    }
  }
  function handleSubmit() {
    let userMessage = prompt.value;
    if (!userMessage || userMessage.trim() === '') return;
    prompt.value = '';
    onConversation(userMessage);
  }
  const handleOutQuestion = (userMessage) => {
    onConversation(userMessage);
  };
  async function onConversation(userMessage) {
    if(!props.type && props.type != 'view'){
      if(appData.value.type && appData.value.type == 'chatSimple' && !appData.value.modelId) {
        messageTip("请选择AI模型");
        return;
      }
      if(appData.value.type && appData.value.type == 'chatFLow' && !appData.value.flowId) {
        messageTip("请选择关联流程");
        return;
      }
      if(!appData.value.name) {
        messageTip("请填写应用名称");
        return;
      }
    }
    
    // 检查是否需要设置额外参数
    if (props.hasExtraFlowInputs) {
      // 检查是否已设置
      if (!props.conversationSettings || Object.keys(props.conversationSettings).length === 0) {
        // 弹出设置弹窗，阻止发送
        message.warning('请先设置对话参数');
        emit('edit-settings');
        return;
      }
    }
    
    if (loading.value) return;
    loading.value = true;

    addChat(uuid.value, {
      dateTime: new Date().toLocaleString(),
      content: userMessage,
      images:uploadUrlList.value?uploadUrlList.value:[],
      files: fileUrlList.value ? fileUrlList.value : [],
      inversion: 'user',
      error: false,
      conversationOptions: null,
      requestOptions: { prompt: userMessage, options: null },
      eventType: 'message',
    });
    scrollToBottom();

    let options: any = {};
    const lastContext = conversationList.value[conversationList.value.length - 1]?.conversationOptions;
    if (lastContext && usingContext.value) {
      options = { ...lastContext };
    }

    addChat(uuid.value, {
      dateTime: new Date().toLocaleString(),
      content: '思考中...',
      loading: true,
      inversion: 'ai',
      error: false,
      conversationOptions: null,
      requestOptions: { prompt: userMessage, options: { ...options } },
      referenceKnowledge: [],
      eventType: 'message',
    });

    scrollToBottom();

    //发送消息
    sendMessage(userMessage,options);
  }

  onUnmounted(() => {
    updateChatSome(uuid.value, chatData.value.length - 1, { loading: false });
  });

  const addChat = (uuid, data) => {
    chatData.value.push({ ...data });
  };
  const updateChat = async (uuid, index, data) => {
    let lastChatData = chatData.value[index];
    if(lastChatData.showAvatar){
      data.showAvatar = lastChatData.showAvatar;
    }
    chatData.value.splice(index, 1, data);
    await scrollToBottom();
  };
  /**
   * 顶置开场白
   * @param txt
   */
  const topChat = (txt) => {
    let data = {
      content: txt,
      key: 'prologue',
      loading: false,
      dateTime: dayjs().format('YYYY/MM/DD HH:mm:ss'),
      inversion: 'ai',
      presetQuestion: props.presetQuestion ? JSON.parse(props.presetQuestion) : "",
    };
    if (chatData.value && chatData.value.length > 0) {
      let key = chatData.value[0].key;
      if (key === 'prologue') {
        chatData.value[0] = { ...data };
        return;
      }
    }
    chatData.value.unshift({ ...data });
  };
  const updateChatSome = (uuid, index, data) => {
    chatData.value[index] = { ...chatData.value[index], ...data };
  };
  const updateChatFail = (uuid, data) => {
    const index = chatData.value.length - 1
    const oldChat = chatData.value[index];
    updateChat(uuid.value, chatData.value.length - 1, {
      ...oldChat,
      // dateTime: new Date().toLocaleString(),
      // content: data,
      inversion: 'ai',
      error: true,
      errorMsg: data,
      loading: true,
      conversationOptions: null,
      requestOptions: null,
      eventType: "message",
    });
    scrollToBottom();
  };

  /**
   * 清空会话
   * @param id
   */
  function handleDelSession (){
    Modal.confirm({
      title: '清空会话',
      icon: createVNode(ExclamationCircleOutlined),
      content: '是否清空会话?',
      closable: true,
      okText: '确定',
      cancelText: '取消',
      wrapClassName:'ai-chat-modal',
      async onOk() {
        try {
          //update-begin---author:wangshuai---date:2025-12-12---for:【QQYUN-14127】【AI】AI应用门户---
          let url = '/airag/chat/messages/clear/' + uuid.value;
          if(props.sessionType){
            url += "/" + props.sessionType;
          }
          defHttp.get({
            url: url,
          //update-end---author:wangshuai---date:2025-12-12---for:【QQYUN-14127】【AI】AI应用门户---
          },{ isTransformResponse: false }).then((res) => {
            if(res.success){
              chatData.value = [];
              topicId.value = "";
              if(props.prologue){
                topChat(props.prologue);
              }
            }
          })
        } catch {
          return console.log('Oops errors!');
        }
      },
    });
  };

  // 停止响应
  const handleStop = () => {
    console.log('ai 聊天：：：---停止响应');
    if (loading.value) {
      loading.value = false;
    }
    updateChatSome(uuid, chatData.value.length - 1, { loading: false });
  };

  handleStop();

  const knowList = ref<Recordable[]>([])

  /**
   * 停止消息
   */
  function handleStopChat() {
    //update-begin---author:wangshuai---date:2025-06-03---for:【issues/8338】AI应用聊天回复stop无效，仍会继续输出回复---
    const currentRequestId = requestId.value
    if(currentRequestId){
      try{
        //调用后端接口停止响应
        defHttp.get({
          url: '/airag/chat/stop/' + currentRequestId,
        },{ isTransformResponse: false });
      } finally {
        handleStop();
        localStorage.removeItem('chat_requestId_' + uuid.value);
      }
      //update-end---author:wangshuai---date:2025-06-03---for:【issues/8338】AI应用聊天回复stop无效，仍会继续输出回复---
    } else {
      localStorage.removeItem('chat_requestId_' + uuid.value);
    }
  }

  /**
   * 读取文本
   * @param message
   * @param options
   */
  async function sendMessage(message, options) {
    let param = {};
    if (!props.type && props.type != 'view') {
      param = {
        content: message,
        images: uploadUrlList.value?uploadUrlList.value:[],
        files: fileUrlList.value ? fileUrlList.value : [],
        topicId: topicId.value,
        app: appData.value,
        responseMode: 'streaming',
        // 添加对话设置参数（调试模式也需要）
        flowInputs: props.conversationSettings || {},
        // 添加网络搜索参数
        enableSearch: enableSearch.value,
        // 添加深度思考参数
        enableThink: enableThink.value,
        // 添加绘画参数
        enableDraw: enableDraw.value,
        drawModelId: enableDraw.value ? drawModelId.value : '',
        // 添加消息类型 portal 门户
        sessionType: props.sessionType || ''
      };
    }else{
      param = {
        content: message,
        topicId: usingContext.value?topicId.value:'',
        images: uploadUrlList.value?uploadUrlList.value:[],
        files: fileUrlList.value ? fileUrlList.value : [],
        appId: appData.value.id,
        responseMode: 'streaming',
        conversationId: uuid.value === "1002"?'':uuid.value,
        // 添加对话设置参数
        flowInputs: props.conversationSettings || {},
        // 添加网络搜索参数
        enableSearch: enableSearch.value,
        // 添加深度思考参数
        enableThink: enableThink.value,
        // 添加绘画参数
        enableDraw: enableDraw.value,
        drawModelId: enableDraw.value ? drawModelId.value : '',
        // 添加消息类型 portal 门户
        sessionType: props.sessionType || ''
      };

      if(headerTitle.value == '新建聊天'){
        headerTitle.value = message.length>10?truncateString(message,10):message
      }

      emit("reload-message-title",message.length>10?truncateString(message,10):message)
    }

    uploadUrlList.value = [];
    fileInfoList.value = [];
    fileUrlList.value = [];
    fileList.value = [];
    knowList.value = [];
    options.message = message;
    const readableStream = await defHttp.post(
      {
        url: props.url,
        params: param,
        adapter: 'fetch',
        responseType: 'stream',
        timeout: 5 * 60 * 1000,
      },
      {
        isTransformResponse: false,
      }
    ).catch((e)=>{
      //update-begin---author:wangshuai---date:2025-04-28---for:【QQYUN-12297】【AI】聊天，超时以后提示---
      if(e.code === 'ETIMEDOUT'){
        updateChatFail(uuid, "当前用户较多，排队中，请稍候再次重试！");
        handleStop();
        return;
      }else{
        updateChatFail(uuid, "服务器错误，请稍后重试！");
        handleStop();
        return;
      }
      console.error(e)
      //update-end---author:wangshuai---date:2025-04-28---for:【QQYUN-12297】【AI】聊天，超时以后提示---
    });
    await renderChatByResult(readableStream,options);
  }
  // 是否使用上下文
  const handleUsingContext = () => {
    usingContext.value = !usingContext.value;
    if (usingContext.value) {
      message.success("当前模式下, 发送消息会携带之前的聊天记录");
    } else {
      message.warning("当前模式下, 发送消息不会携带之前的聊天记录");
    }
  };

  /**
   * 提示
   * @param value
   */
  function messageTip(value) {
    message.warning(value);
  }

  /**
   * 渲染文本
   * @param item
   * @param conversationId
   * @param text
   * @param options
   */
  async function renderText(item,conversationId,text,options) {
    let returnText = "";
    if (item.event == 'MESSAGE' || item.event == 'THINKING' || item.event == 'THINKING_END') {
      let message = item.data?.message ?? "";
      let messageText = "";
      //update-begin---author:wangshuai---date:2025-04-24---for:应该先判断是否包含card---
      if(message && message.indexOf("::card::") !== -1){
        messageText = message;
      } else if(message && message.indexOf("::cardConfig::") !== -1) {
        messageText = message;
      } else {
        text = text + message;
        messageText = text;
        returnText = text;
      }
      //update-end---author:wangshuai---date:2025-04-24---for:应该先判断是否包含card---
      // 从消息中获取 requestId
      if (item.requestId) {
        requestId.value = item.requestId;
      }
      if(item.event == 'THINKING'){
        isThinking.value = true;
      }
      if(item.event == 'MESSAGE' && isThinking.value){
        text = item.data.message;
        returnText = item.data.message;
        //发送用户消息
        addChat(uuid.value, {
          dateTime: new Date().toLocaleString(),
          content: item.data.message,
          images:uploadUrlList.value?uploadUrlList.value:[],
          files: fileUrlList.value ? fileUrlList.value : [],
          inversion: 'ai',
          error: false,
          conversationOptions: null,
          requestOptions: { prompt: message, options: null },
          eventType: 'message',
          showAvatar: 'no'
        });
        isThinking.value = false;
        return { returnText, conversationId };
      }
      //更新聊天信息
      updateChat(uuid.value, chatData.value.length - 1, {
        dateTime: new Date().toLocaleString(),
        content: messageText,
        inversion: 'ai',
        error: false,
        loading: item.event == 'THINKING_END' ? false: true,
        conversationOptions: { conversationId: conversationId, parentMessageId: topicId.value },
        requestOptions: { prompt: message, options: { ...options } },
        referenceKnowledge: knowList.value,
        eventType: item.event.toLowerCase(),
      });
    }
    if(item.event == 'INIT_REQUEST_ID'){
      if (item.requestId && props.url != "/airag/app/debug") {
        requestId.value = item.requestId;
        localStorage.setItem('chat_requestId_' + uuid.value, JSON.stringify({ requestId: item.requestId, message: options.message }));
      }
    }
    if (item.event == 'MESSAGE_END') {
      topicId.value = item.topicId;
      conversationId = item.conversationId;
      uuid.value = item.conversationId;
      localStorage.removeItem('chat_requestId_' + uuid.value);
      handleStop();
    }
    if (item.event == 'FLOW_FINISHED') {
      //update-begin---author:wangshuai---date:2025-03-07---for:【QQYUN-11457】聊天调用流程，执行失败了但是没提示---
      if(item.data && !item.data.success){
        updateChatFail(uuid, item.data.message?item.data.message:'请求出错，请稍后重试！');
        localStorage.removeItem('chat_requestId_' + uuid.value);
        handleStop();
        return "";
      }
      //update-end---author:wangshuai---date:2025-03-07---for:【QQYUN-11457】聊天调用流程，执行失败了但是没提示---
      topicId.value = item.topicId;
      conversationId = item.conversationId;
      uuid.value = item.conversationId;
      requestId.value = item.requestId;
      localStorage.removeItem('chat_requestId_' + uuid.value);
      handleStop();
    }
    if (item.event == 'ERROR') {
      updateChatFail(uuid, item.data.message?item.data.message:'请求出错，请稍后重试！');
      localStorage.removeItem('chat_requestId_' + uuid.value);
      handleStop();
      return "";
    }

    // 工具调用开始
    if (item.event == 'TOOL_EXEC_BEFORE') {
      currentToolTag.value = item.data?.message ?? '';
    }

    // 工具调用结束
    if (item.event == 'TOOL_EXEC_DONE') {
      currentToolTag.value = '';
    }

    //update-begin---author:wangshuai---date:2025-03-21---for:【QQYUN-11495】【AI】实时展示当前思考进度---
    if(item.event === "NODE_STARTED"){
      if(!item.data || item.data.type !== 'end'){
        let aiText = "";
        if(item.data.type === 'llm' || item.data.type === 'reply'){
          aiText = "正在构建响应内容";
        }
        if(item.data.type === 'knowledge'){
          aiText = "正在对知识库进行深度检索";
        }
        if(item.data.type === 'classifier'){
          aiText = "正在分类";
        }
        if(item.data.type === 'code'){
          aiText = "正在实施代码运行操作";
        }
        if(item.data.type === 'subflow'){
          aiText = "正在运行子流程";
        }
        if(item.data.type === 'enhanceJava'){
          aiText = "正在执行java增强";
        }
        if(item.data.type === 'http'){
          aiText = "正在发送http请求";
        }
        if(!text){
          //更新聊天信息
          updateChat(uuid.value, chatData.value.length - 1, {
            dateTime: new Date().toLocaleString(),
            retrievalText: aiText,
            text:"",
            inversion: 'ai',
            error: false,
            loading: true,
            conversationOptions: null,
            requestOptions: { prompt: message, options: { ...options } },
            referenceKnowledge: knowList.value,
            eventType: 'message',
          });
        }
      }
    }
    //update-end---author:wangshuai---date:2025-03-21---for:【QQYUN-11495】【AI】实时展示当前思考进度---
    else if (item.event === 'NODE_FINISHED') {
      if(!item.data || item.data.type !== 'end'){
        if(item.data.type === 'knowledge'){
          const id = item.data.id;
          const data = item.data.outputs[id + ".documents"]
          knowList.value = data
          //更新聊天信息
          updateChatSome(uuid.value, chatData.value.length - 1, {referenceKnowledge: knowList.value})
        }
      }
    }
    if(!returnText && item.event !== 'NODE_FINISHED'){
      returnText = text;
    }
    return { returnText, conversationId };
  }

  //上传文件列表集合
  const uploadUrlList = ref<any>([]);
  //文件集合
  const fileInfoList = ref<any>([]);

  /**
   * 文件上传回调事件
   * @param info
   */
  function handleChange(info) {
    let { fileList, file } = info;
    fileInfoList.value = fileList;
    if (file.status === 'error' || (file.response && file.response.code == 500)) {
      message.error(file.response?.message || `${file.name} 上传失败,请查看服务端日志`);
      return;
    }
    if (file.status === 'done') {
      uploadUrlList.value.push(file.response.message);
    }
  }

  /**
   * 获取图片地址
   *
   * @param url
   */
  function getImage(url) {
    return getFileAccessHttpUrl(url);
  }

  /**
   * 上传前事件
   */
  function beforeUpload(file) {
    var fileType = file.type;
    if (fileType === 'image') {
      if (fileType.indexOf('image') < 0) {
        message.warning('请上传图片');
        return false;
      }
    }
    if(uploadUrlList.value && uploadUrlList.value.length > 2){
      message.warning("最多只能上传三张！");
      return false;
    }
    return true;
  }

  /**
   * 删除图片
   */
  function deleteImage(index) {
    uploadUrlList.value.splice(index,1);
    fileInfoList.value.splice(index,1);
  }

  /**
   * 图片预览
   * @param url
   */
  function handlePreview(url){
    const onImgLoad = ({ index, url, dom }) => {
      console.log(`第${index + 1}张图片已加载，URL为：${url}`, dom);
    };
    let imageList = [getImage(url)];
    createImgPreview({ imageList: imageList, defaultWidth: 700, rememberState: true, onImgLoad });
  }

  /**
   * 截取字符串
   * @param str
   * @param maxLength
   */
  function truncateString(str, maxLength) {
    if (str.length <= maxLength){
      return str;
    }
    let chineseCount = 0;
    let englishCount = 0;
    let digitCount = 0;
    let result = '';
    for (let i = 0; i < str.length; i++) {
      const char = str[i];
      if (/[\u4e00-\u9fa5]/.test(char)) { // 判断是否为汉字
        chineseCount++;
      } else if (/[a-zA-Z]/.test(char)) { // 判断是否为英文字母
        englishCount++;
      } else if (/\d/.test(char)) { // 判断是否为数字
        digitCount++;
      }
      if (chineseCount + englishCount / 2 + digitCount / 2 > maxLength) {
        break;
      }
      result += char;
    }

    return result;
  }

  /**
   * 粘贴事件
   * @param event
   */
  function paste(event) {
    if(uploadUrlList.value && uploadUrlList.value.length > 2){
      message.warning("最多只能上传三张！");
      return;
    }
    const items = (event.clipboardData || window.clipboardData).items;
    if (!items || items.length === 0){
      //说明浏览器不支持复制图片
      message.error('当前浏览器不支持本地打开图片！');
      return;
    }
    let image = null;
    for (let i = 0; i < items.length; i++) {
      if (items[i].type.indexOf('image') !== -1) {
        image = items[i].getAsFile();
        handleUploadImage(image);
        break;
      }
    }
  }

  /**
   * 粘贴图片
   * @param image
   */
  async function handleUploadImage(image) {
    const isReturn = (fileInfo) => {
      try {
        if (fileInfo.code === 0) {
          let { message } = fileInfo;
          uploadUrlList.value.push(message);
          fileInfoList.value.push(image);
        } else if (fileInfo.code === 500 || fileInfo.code === 510) {
          message.error(fileInfo.message || `${image.name} 导入失败`);
        }
      } catch (error) {
        console.log('导入的数据异常', error);
        message.error(`${image.name} 导入失败`);
      }
    };
    await defHttp.uploadFile({ url: "/airag/chat/upload" }, { file: image }, { success: isReturn });
  }

  /**
   * 渲染返回来的结果
   * @param readableStream
   * @param options
   */
  async function renderChatByResult(readableStream, options) {
    const reader = readableStream.getReader();
    const decoder = new TextDecoder('UTF-8');
    let conversationId = '';
    let buffer = '';
    let text = ''; // 按 SSE 协议分割消息
    while (true) {
      const { done, value } = await reader.read();
      if (done) {
        break;
      }
      //update-begin---author:wangshuai---date:2025-03-12---for:【QQYUN-11555】聊天时要流式显示消息---
      let result = decoder.decode(value, { stream: true });
      result = buffer + result;
      const lines = result.split('\n\n');
      for (let line of lines) {
        if (line.startsWith('data:')) {
          let content = line.replace('data:', '').trim();
          if(!content){
            continue;
          }
          if(!content.endsWith('}')){
            buffer = buffer + content;
            continue;
          }
          buffer = "";
          try {
            //update-begin---author:wangshuai---date:2025-03-13---for:【QQYUN-11572】发布到线上不能实时动态，内容不能加载出来，得刷新才能看到全部回答---
            if(content.indexOf(":::card:::") !== -1){
              content = content.replace(/\s+/g, '');
            }
            let parse = JSON.parse(content);
            await renderText(parse,conversationId,text,options).then((res)=>{
              text = res.returnText;
              conversationId = res.conversationId;
            });
            //update-end---author:wangshuai---date:2025-03-13---for:【QQYUN-11572】发布到线上不能实时动态，内容不能加载出来，得刷新才能看到全部回答---
          } catch (error) {
            console.log('Error parsing update:', error);
          }
          //update-end---author:wangshuai---date:2025-03-12---for:【QQYUN-11555】聊天时要流式显示消息---
        }else{
          if(!line){
            continue;
          }
          if(!line.endsWith('}')){
            buffer = buffer + line;
            continue;
          }
          buffer = "";
          //update-begin---author:wangshuai---date:2025-03-13---for:【QQYUN-11572】发布到线上不能实时动态，内容不能加载出来，得刷新才能看到全部回答---
          try {
            if(line.indexOf(":::card:::") !== -1){
              line = line.replace(/\s+/g, '');
            }
            let parse = JSON.parse(line);
            await renderText(parse, conversationId, text, options).then((res) => {
              text = res.returnText;
              conversationId = res.conversationId;
            });
          } catch (error) {
            console.log('Error parsing update:', error);
          }
          //update-end---author:wangshuai---date:2025-03-13---for:【QQYUN-11572】发布到线上不能实时动态，内容不能加载出来，得刷新才能看到全部回答---
        }
      }
    }
    //update-begin---author:wangshuai---date:2025-11-05---for: 如果是断线重连并且文本为空，需要移出前面两条会话---
    if(!text && isReConnect && chatData.value.length >1){
      //如果是断线重连的情况下，流结果为空时，移除占位的AI消息，避免空结果也新增聊天记录
      const lastMsg = chatData.value[chatData.value.length - 1];
      if (lastMsg && lastMsg.inversion === 'ai' && lastMsg.content === '请稍后') {
        chatData.value.splice(chatData.value.length - 1, 1);
        chatData.value.splice(chatData.value.length - 1, 1);
      }
    //update-end---author:wangshuai---date:2025-11-05---for: 如果是断线重连并且文本为空，需要移出前面两条会话---
      localStorage.removeItem('chat_requestId_' + uuid.value);
      loading.value = false;
    }
  }

  /**
   * ai重连
   */
  async function aiReConnection() {
    //查询requestId
    let chat = localStorage.getItem("chat_requestId_" + uuid.value);
    if(chat) {
      let array = JSON.parse(chat);
      let message = array.message;
      let requestId = array.requestId;
      const result = await defHttp.get({ url: '/airag/chat/receive/' + requestId ,
        adapter: 'fetch',
        responseType: 'stream',
        timeout: 5 * 60 * 1000
      }, { isTransformResponse: false }).catch(async (err)=>{
        loading.value = false;
        localStorage.removeItem('chat_requestId_' + uuid.value);
      });
      if(result && message){
        loading.value = true;
        isReConnect.value = true;
        //发送用户消息
        addChat(uuid.value, {
          dateTime: new Date().toLocaleString(),
          content: message,
          images:uploadUrlList.value?uploadUrlList.value:[],
          inversion: 'user',
          error: false,
          conversationOptions: null,
          requestOptions: { prompt: message, options: null },
          eventType: 'message',
        });
        let options: any = {};
        const lastContext = conversationList.value[conversationList.value.length - 1]?.conversationOptions;
        if (lastContext && usingContext.value) {
          options = { ...lastContext };
        }
        //添加ai消息
        addChat(uuid.value, {
          dateTime: new Date().toLocaleString(),
          content: '请稍后',
          loading: false,
          inversion: 'ai',
          error: false,
          conversationOptions: null,
          requestOptions: { prompt: message, options: { ...options } },
          referenceKnowledge: [],
          eventType: 'message',
        });
        options.message = message;
        scrollToBottom();
        //流式输出
        await renderChatByResult(result,options);
      } else {
        loading.value = false;
        localStorage.removeItem('chat_requestId_' + uuid.value);
        isReConnect.value = false;
      }
    } else {
      isReConnect.value = false;
    }
  }

  //监听开场白
  watch(
    () => props.prologue,
    (val) => {
      try {
        if (val) {
          topChat(val);
        }
      } catch (e) {}
    }
  );

  //监听开场白预制问题
  watch(
    () => props.presetQuestion,
    (val) => {
      topChat(props.prologue);
    }
  );

  //监听应用信息
  watch(
    () => props.formState,
    (val) => {
      try {
        if (val) {
          appData.value = val;
          // 检查模型是否支持网络搜索
          checkModelProvider();
        }
      } catch (e) {}
    },
    { deep: true, immediate: true }
  );

  // 编辑对话设置
  function handleEditSettings() {
    emit('edit-settings');
  }

  // 切换网络搜索
  function toggleWebSearch() {
    enableSearch.value = !enableSearch.value;
    if (enableSearch.value) {
      message.success("已开启联网搜索");
    } else {
      message.info("已关闭联网搜索");
    }
  }

  /**
   * 切换网络思考
   */
  function toggleThink() {
    enableThink.value = !enableThink.value;
    if (enableThink.value) {
      message.success("已开启深度思考");
    } else {
      message.info("已关闭深度思考");
    }
  }

  // 检查模型是否支持网络搜索（从appData.metadata.modelInfo中获取）
  function checkModelProvider() {
    if (appData.value && appData.value.metadata) {
      try {
        const metadata = typeof appData.value.metadata === 'string' 
          ? JSON.parse(appData.value.metadata) 
          : appData.value.metadata;

        //是否显示绘图工具
        showDraw.value = metadata.izDraw === '1';
        //是否选中生成图片
        enableDraw.value = metadata.izDraw === '1';
        drawModelId.value = metadata.drawModelId;

        if (metadata && metadata.modelInfo) {
          modelProvider.value = metadata.modelInfo.provider || '';
          modelName.value = metadata.modelInfo.modelName || '';
          // 只有千问模型支持网络搜索
          showWebSearch.value = modelProvider.value === 'QWEN';
          showThink.value = modelName.value === 'deepseek-reasoner';
        } else {
          showWebSearch.value = false;
          showThink.value = false;
        }
      } catch (e) {
        console.error('解析模型信息失败', e);
        showWebSearch.value = false;
        showThink.value = false;
      }
    } else {
      showWebSearch.value = false;
      showThink.value = false;
      showDraw.value = false;
    }
  }

  /**
   * 生成图片
   */
  function handleGenerateImage() {
    enableDraw.value = !enableDraw.value;
    if (enableDraw.value) {
      message.success("已开启生成图片");
    } else {
      message.info("已关闭生成图片");
    }
  }

  //================================================== begin 【QQYUN-14261】AI助手，支持多模态能力- 文档 ====================================
  /**
   * 通用文件上传前校验
   * 
   * @param file
   */
  function beforeUploadFile(file) {
    const fileName = file.name;
    const fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    const allowFileTypes = ['txt', 'pdf', 'docx', 'doc', 'pptx', 'ppt', 'xlsx', 'xls', 'md'];
    if (allowFileTypes.indexOf(fileType) === -1) {
      message.warning('不支持该文件类型上传，请上传 txt, pdf, docx, doc, pptx, ppt, xlsx, xls, md 格式文件');
      return false;
    }
    return true;
  }

  /**
   * 文件上传（非图片）
   * @param info
   */
  function handleChangeFile(info) {
    let { file, fileList: newFileList } = info;
    fileList.value = newFileList;
    if (file.status === 'error' || (file.response && file.response.code == 500)) {
      message.error(file.response?.message || `${file.name} 上传失败,请查看服务端日志`);
      return;
    }
    fileUrlList.value = fileList.value
        .filter(item => item.status === 'done' && item.response)
        .map(item => item.response.message);
  }

  /**
   * 删除文件
   */
  function deleteFile(index) {
    fileList.value.splice(index, 1);
    fileUrlList.value = fileList.value
        .filter(item => item.status === 'done' && item.response)
        .map(item => item.response.message);
  }
  
  //================================================== end 【QQYUN-14261】AI助手，支持多模态能力- 文档 ====================================
  
  //监听历史信息
  watch(
    () => props.historyData,
    (val) => {
      try {
        //update-begin---author:wangshuai---date:2025-03-06---for:【QQYUN-11384】浏览器打开应用开场白丢了---
        if (val && val.length > 0) {
          chatData.value = cloneDeep(val);
          if(chatData.value[0]){
            topicId.value = chatData.value[0].topicId
          }
        }else{
          chatData.value = [];
          headerTitle.value = props.chatTitle;
        }
        //update-begin---author:wangshuai---date:2025-11-18---for:【QQYUN-14049】【AI】没有开场白，就不展示预设问题了---
        if((props.prologue || props.presetQuestion) && props.chatTitle){
        //update-end---author:wangshuai---date:2025-11-18---for:【QQYUN-14049】【AI】没有开场白，就不展示预设问题了---
          topChat(props.prologue)
        }
        //ai回复重连
        aiReConnection();
      } catch (e) {
        console.log(e)
      }
      //update-end---author:wangshuai---date:2025-03-06---for:【QQYUN-11384】浏览器打开应用开场白丢了---
    },
    { deep: true, immediate: true }
  );

  onMounted(() => {
    scrollToBottom();
    uploadUrlList.value = [];
    fileInfoList.value = [];
    fileUrlList.value = [];
    fileList.value = [];
    // 检查模型是否支持网络搜索
    checkModelProvider();
  });
</script>

<style lang="less" scoped>
  .chatWrap {
    width: 100%;
    height: 100%;
    padding: 20px;
    .content {
      height: 100%;
      width: 100%;
      background: #fff;
      display: flex;
      flex-direction: column;
    }
  }
  .main {
    flex: 1;
    min-height: 0;
    .scrollArea {
      overflow-y: auto;
      height: 100%;
    }
    .chatContentArea {
      padding: 10px;
    }
  }
  .emptyArea {
    display: flex;
    justify-content: center;
    align-items: center;
    color: #d4d4d4;
  }
  .stopArea {
    display: flex;
    justify-content: center;
    padding: 10px 0;
  }
  .footer {
    display: flex;
    flex-direction: column;
    padding: 6px 16px;
    .topArea {
      padding-left: 6%;
      margin-bottom: 6px;
    }
    .bottomArea {
      display: flex;
      align-items: center;

      .ant-input {
        margin: 0 8px;
      }
      .ant-input,
      .ant-btn {
        height: 36px;
      }
      textarea.ant-input {
        padding-top: 6px;
        padding-bottom: 6px;
      }
      .contextBtn,
      .delBtn {
        padding: 0;
        width: 40px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .delBtn {
        margin-right: 8px;
      }
      .contextBtn {
        color: #a8071a;
        &.enabled {
          color: @primary-color;
        }
        font-size: 18px;
      }
      .sendBtn {
        font-size: 14px;
        width: 100%;
        display: flex;
        padding: 4px 6px;
        align-items: center;
        &.enabled {
          color: #0a66ff !important;
        }
      }
      .webSearchBtn {
        border-radius: 8px;
        padding: 4px 8px;
        height: 30px;
        background-color: transparent;
        border: 1px solid transparent;
        color: #3d4353;
        transition: all .2s ease;
        :deep(.anticon){
          margin-right: 6px;
          color: #3d4353;
        }
        &:hover{
          border-color: #d2d7e5;
          background-color: #f7f9fc;
        }
        &.enabled {
          background-color: rgba(10,102,255,0.08);
          border-color: #0a66ff;
          color: #0a66ff;
          box-shadow: none;
          font-weight: 500;
          :deep(.anticon) {
            color: #0a66ff !important;
          }
        }
      }
      .stopBtn {
        width: 32px;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 8px;
      }
    }
  }
  :deep(.chatgpt .markdown-body) {
    background-color: #f4f6f8;
  }
  :deep(.ant-message) {
    top: 50% !important;
  }
  .header-title{
    color: #101828;
    font-size: 16px;
    font-weight: 400;
    padding-bottom: 8px;
    margin-left: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 30px;
    
    .title-content{
      display: flex;
      align-items: center;
      gap: 4px;
      overflow: hidden;
      
      > span{
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    
    .header-actions{
      display: flex;
      align-items: center;
      gap: 8px;
      flex-shrink: 0;
    }
    
    .edit-btn{
      padding: 2px 4px;
      color: #999;
      flex-shrink: 0;
      height: 24px;
      
      &:hover{
        color: @primary-color;
      }
      
      :deep(.anticon){
        font-size: 16px;
      }
    }
    
    .header-advertisint{
      display:flex;
      margin-right: 20px;
      font-size: 12px;
    }
  }
  .chat-textarea{
    display: flex;
    align-items: center;
    width: 100%;
    border-radius: 15px;
    border-style: solid;
    border-width: 1px;
    flex-direction: column;
    transition: width 0.3s;
    border-color: #d2d7e5;
    .textarea-top{
      border-bottom: 1px solid #f0f0f5;
      padding: 12px 28px;
      width: 100%;
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      .top-image{
        display: flex;
        img{
          border-radius: 8px;
          cursor: pointer;
          height: 60px;
          position: relative;
          width: 60px;
        }
      }
      /*begin 文件的样式*/
      .file-card {
        display: flex;
        align-items: center;
        background: #f4f6f8;
        border-radius: 8px;
        padding: 8px 12px;
        width: 200px;
        position: relative;
        
        .file-card-icon {
          width: 32px;
          height: 32px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 8px;
          .file-thumb {
            width: 32px;
            height: 32px;
            border-radius: 4px;
            object-fit: cover;
          }
        }
        
        .file-card-info {
          flex: 1;
          overflow: hidden;
          .file-name {
            font-size: 14px;
            color: #333;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          .file-size {
            font-size: 12px;
            color: #999;
          }
        }
        
        .file-card-close {
          position: absolute;
          top: -6px;
          right: -6px;
          width: 16px;
          height: 16px;
          background: #ccc;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          cursor: pointer;
          color: #fff;
          font-size: 10px;
          opacity: 0;
          transition: opacity 0.2s;
          
          &:hover {
            background: #ff4d4f;
          }
        }
        
        &:hover .file-card-close {
          opacity: 1;
        }
        /*end 文件的样式*/
      }
    }
    .textarea-bottom{
      display: flex;
      flex-direction: column;
      flex: 1 1;
      min-height: 48px;
      position: relative;
      padding: 2px 10px;
      width: 100%;
      /*begin 底部样式*/
      .textarea-action-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        margin-top: 8px;

        .left-actions {
          display: flex;
          align-items: center;
          gap: 4px;
        }
        .right-actions {
          display: flex;
          align-items: center;
          gap: 4px;
        }
        .sendBtn {
          width: auto;
          padding: 4px 6px;
          height: 30px;
        }
      }
      /*end 底部样式*/
    }
  }
  .chat-textarea:hover{
    border-color: #9dc1fb;
  }
  .textarea-active{
    border-color: #98bdfa !important;
  }
  :deep(.ant-divider-vertical){
    margin: 0 2px;
  }
  .upload-icon{
    cursor: pointer;
    position: absolute;
    background-color: #1D1C23;
    color: white;
    border-radius: 50%;
    padding: 4px;
    display: none;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 4px #e6e6e6;
    margin-left: 44px;
    margin-top: -4px;
  }
  .top-image:hover{
    .upload-icon{
      display: flex;
    }
  }
  
  @media (max-width: 600px) {
    //手机下的样式 平板不需要调整
    .footer{
      padding: 0;
      .bottomArea{
        .delBtn{
          margin-right: 0;
        }
      }
    }
    .chatWrap{
      padding: 10px 10px 10px 0;
    }
    .main .chatContentArea{
      padding: 10px 0 0 10px;
    }
  }
</style>
<style lang="less">
 .ai-chat-modal{
   z-index: 9999 !important;
 }
 .ai-chat-message{
   z-index: 9999 !important;
 }

 .chat-upload-dropdown .ant-dropdown-menu{
   border-radius: 10px;
   padding: 6px 4px;
 }
 .chat-upload-dropdown .ant-dropdown-menu-item:hover{
   background-color: #f0f6ff;
 }
</style>
