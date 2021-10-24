<template>
  <div class="wrapper">
    <!-- 헤더 -->
    <Header />  
    <div class="content-view">
      <router-view></router-view>
    </div>
    <Footer />
  </div>
</template>
<script>
import Header from '@/components/layout/Header'
import Footer from '@/components/layout/Footer'

export default {
  components: {Header, Footer},
  name: "TheContainer"
}
</script>