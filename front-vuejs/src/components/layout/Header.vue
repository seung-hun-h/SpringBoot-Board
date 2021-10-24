<template>
	<header>
		<h1><a href="/" class="logo"><img alt="Vue logo" src="../../assets/logo.png" width="80"></a></h1>
		<div class="menuWrap">
			<ul class="menu">
                <li><router-link to="/posts">게시판</router-link></li>
				<li v-if="user === null"><router-link to="/join">회원가입</router-link></li>
				<li v-if="user === null"><router-link to="/login">로그인</router-link></li>
				<li v-else><div type="button" @click="logout()">로그아웃</div></li>
			</ul>
		</div>
	</header>
</template>

<script>
export default {
	name: "TopBar",
	data() {
		return {
			user: JSON.parse(window.localStorage.getItem("user"))
		}
	},
	methods: {
		logout() {
			let params = {
				email: this.user.email
			}
			console.log(params)
			this.axios.post(`http://localhost:8080/api/v1/users/logout`,
			JSON.stringify(params), {headers: { 'content-type': 'application/json' }}
			).then(() => {
				alert("로그아웃 되었습니다")
				this.user = null;
				window.localStorage.removeItem("user");
				this.$router.push("/posts");
			}).catch(e => {
				alert(e.response.data.data)
			})
		
		}
	}
}
</script>

<style scoped>
header{width:100%; text-align:center; position:relative; height:120px; border-bottom:1px solid #35495e}
header h1{position:absolute; top:0; left:100px;}
header ul.menu:after{display:block; clear:both; content:'';}
header ul.menu{position:absolute; top:20px; right:50px;}
header ul.menu li{float:left; padding:10px 20px; list-style:none;}

a{text-decoration:none; color:#333;}
</style>