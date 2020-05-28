<template>
  <div class="page-header-index-wide page-header-wrapper-grid-content-main">

    <a-row :gutter="24">
      <a-col :md="24" :lg="7">
        <a-card :bordered="false">
          <div class="account-center-avatarHolder">
            <div class="avatar">
              <img :src="getAvatar()"/>
            </div>
            <div class="username">{{ nickname() }}</div>
            <div class="bio">海纳百川，有容乃大</div>
          </div>
          <div class="account-center-detail">
            <p>
              <i class="title"></i>交互专家
            </p>
            <p>
              <i class="group"></i>蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED
            </p>
            <p>
              <i class="address"></i><span>浙江省</span><span>杭州市</span>
            </p>
          </div>
          <a-divider />

          <div class="account-center-tags">
            <div class="tagsTitle">标签</div>
            <div>
              <template v-for="(tag, index) in tags">
                <a-tooltip v-if="tag.length > 20" :key="tag" :title="tag">
                  <a-tag :key="tag" :closable="index !== 0" :afterClose="() => handleTagClose(tag)">
                    {{ `${tag.slice(0, 20)}...` }}
                  </a-tag>
                </a-tooltip>
                <a-tag v-else :key="tag" :closable="index !== 0" :afterClose="() => handleTagClose(tag)">{{ tag }}</a-tag>
              </template>
              <a-input
                v-if="tagInputVisible"
                ref="tagInput"
                type="text"
                size="small"
                :style="{ width: '78px' }"
                :value="tagInputValue"
                @change="handleInputChange"
                @blur="handleTagInputConfirm"
                @keyup.enter="handleTagInputConfirm"
              />
              <a-tag v-else @click="showTagInput" style="background: #fff; borderStyle: dashed;">
                <a-icon type="plus" /> New Tag
              </a-tag>
            </div>
          </div>
          <a-divider :dashed="true" />

          <div class="account-center-team">
            <div class="teamTitle">团队</div>
            <a-spin :spinning="teamSpinning">
              <div class="members">
                <a-row>
                  <a-col :span="12" v-for="(item, index) in teams" :key="index">
                    <a>
                      <a-avatar size="small" :src="item.avatar" />
                      <span class="member">{{ item.name }}</span>
                    </a>
                  </a-col>
                </a-row>
              </div>
            </a-spin>
          </div>
        </a-card>
      </a-col>
      <a-col :md="24" :lg="17">
        <a-card
          style="width:100%"
          :bordered="false"
          :tabList="tabListNoTitle"
          :activeTabKey="noTitleKey"
          @tabChange="key => handleTabChange(key, 'noTitleKey')"
        >
          <article-page v-if="noTitleKey === 'article'"></article-page>
          <app-page v-else-if="noTitleKey === 'app'"></app-page>
          <project-page v-else-if="noTitleKey === 'project'"></project-page>
        </a-card>
      </a-col>
    </a-row>


  </div>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import RouteView from "@/components/layouts/RouteView"
  import { AppPage, ArticlePage, ProjectPage } from './page'
  import { mapGetters } from 'vuex'
  import { getFileAccessHttpUrl } from '@/api/manage';

  export default {
    components: {
      RouteView,
      PageLayout,
      AppPage,
      ArticlePage,
      ProjectPage
    },
    data() {
      return {
        tags: ['很有想法的', '专注设计', '辣~', '大长腿', '川妹子', '海纳百川'],

        tagInputVisible: false,
        tagInputValue: '',

        teams: [],
        teamSpinning: true,

        tabListNoTitle: [{
            key: 'article',
            tab: '文章(8)',
          }, {
            key: 'app',
            tab: '应用(8)',
          }, {
            key: 'project',
            tab: '项目(8)',
          }
        ],
        noTitleKey: 'app',
      }
    },
    mounted () {
      this.getTeams()
    },
    methods: {
      ...mapGetters(["nickname", "avatar"]),
      getAvatar(){
          return getFileAccessHttpUrl(this.avatar());
      },
      getTeams() {
        this.$http.get('/api/workplace/teams')
          .then(res => {
            this.teams = res.result
            this.teamSpinning = false
          })
      },

      handleTabChange (key, type) {
        this[type] = key
      },

      handleTagClose (removeTag) {
        const tags = this.tags.filter(tag => tag != removeTag)
        this.tags = tags
      },

      showTagInput () {
        this.tagInputVisible = true
        this.$nextTick(() => {
          this.$refs.tagInput.focus()
        })
      },

      handleInputChange (e) {
        this.tagInputValue = e.target.value
      },

      handleTagInputConfirm () {
        const inputValue = this.tagInputValue
        let tags = this.tags
        if (inputValue && tags.indexOf(inputValue) === -1) {
          tags = [...tags, inputValue]
        }

        Object.assign(this, {
          tags,
          tagInputVisible: false,
          tagInputValue: ''
        })
      }
    },
  }
</script>

<style lang="less" scoped>
  .page-header-wrapper-grid-content-main {
    width: 100%;
    height: 100%;
    min-height: 100%;
    transition: .3s;

    .account-center-avatarHolder {
      text-align: center;
      margin-bottom: 24px;

      & > .avatar {
        margin: 0 auto;
        width: 104px;
        height: 104px;
        margin-bottom: 20px;
        border-radius: 50%;
        overflow: hidden;
        img {
          height: 100%;
          width: 100%;
        }
      }

      .username {
        color: rgba(0, 0, 0, 0.85);
        font-size: 20px;
        line-height: 28px;
        font-weight: 500;
        margin-bottom: 4px;
      }
    }

    .account-center-detail {

      p {
        margin-bottom: 8px;
        padding-left: 26px;
        position: relative;
      }

      i {
        position: absolute;
        height: 14px;
        width: 14px;
        left: 0;
        top: 4px;
        background: url(https://gw.alipayobjects.com/zos/rmsportal/pBjWzVAHnOOtAUvZmZfy.svg)
      }

      .title {
        background-position: 0 0;
      }
      .group {
        background-position: 0 -22px;
      }
      .address {
        background-position: 0 -44px;
      }
    }

    .account-center-tags {
      .ant-tag {
        margin-bottom: 8px;
      }
    }

    .account-center-team {

      .members {
        a {
          display: block;
          margin: 12px 0;
          line-height: 24px;
          height: 24px;
          .member {
            font-size: 14px;
            color: rgba(0, 0, 0, .65);
            line-height: 24px;
            max-width: 100px;
            vertical-align: top;
            margin-left: 12px;
            transition: all 0.3s;
            display: inline-block;
          }
          &:hover {
            span {
              color: #1890ff;
            }
          }
        }
      }
    }

    .tagsTitle, .teamTitle {
      font-weight: 500;
      color: rgba(0,0,0,.85);
      margin-bottom: 12px;
    }

  }

</style>