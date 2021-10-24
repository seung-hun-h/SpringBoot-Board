<template>
  <div class="container">
      <div class="card">
        <div class="card-body">
          <Table :list="list"
                 :cnt="totalCnt"
                 :getData="getPosts"
                  id="table" >
            <template v-slot:header>
              <th>#</th>
              <th class="text-center">제목</th>
              <th class="text-center">작성자</th>
              <th class="text-center">작성일</th>
              <th class="text-center">조회수</th>
            </template>
                <template v-slot:default="slotProps">
                <td class="text-center">{{slotProps.row.postId}}</td>
                <td class="text-center">
                    <router-link :to="{ path:`/posts/${slotProps.row.postId}`}">
                        {{slotProps.row.title}}
                    </router-link>
                </td>
                <td class="text-center">{{slotProps.row.createdBy}}</td>
                <td class="text-center">{{slotProps.row.createdAt}}</td>
                <td class="text-center">
                </td>
                </template> 
          </Table>
        </div>
      </div>

      <div class="row mt-3 float-right" v-if="user !== null">
        <div class="col-auto">
          <router-link :to="{path:'/posts/reg'}"
                        class="btn btn-primary">
            <i class="fa fa-plus">등록</i>
          </router-link>
        </div>
      </div>

  </div>
</template>

<script>
import Table from '@/components/layout/Table'
export default {
  name: 'Posts',
  components: {Table},
  data () {
    return {
      posts: [],
      cnt : 0,
      user: window.localStorage.getItem("user")
    }
  },
  computed: {
    list () {
      return this.posts
    },
    totalCnt () {
      return this.cnt
    }
  },
  mounted () {
    this.handleService()
  },
  methods: {
    handleService() {
      var params = new URLSearchParams()
      params.append("page", 1)
      params.append("size", 10)
      params.append("direction", "desc")
      this.getPosts(params)
    },
    getPosts(params) {
      this.axios.get("http://localhost:8080/api/v1/posts?" + params)
      .then(response => {
        this.posts = response.data.data.posts
        this.cnt = response.data.data.totalElements
      }).catch(e => {
        alert(e)
      })
    },
  }
}
</script>