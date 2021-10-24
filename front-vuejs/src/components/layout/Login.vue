<template>
    <div class="text-center container">
        <form class="form-signin">
            <img class="mb-4" src="../../assets/logo.png" alt="" width="72" height="72">
            <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
            <label for="email" class="sr-only">Email address</label>
            <input v-model="email" type="email" id="email" name="email" class="form-control" placeholder="Email address" required>
            <label for="password" class="sr-only">Password</label>
            <input v-model="password" type="password" id="password" class="form-control" placeholder="Password" required>
            <button class="btn btn-lg btn-primary btn-block" type="button" @click="login()">Sign in</button>
        </form>
    </div>
</template>

<script>
export default {
    name: "Login",
    data() {
        return {
            email: "",
            password: ""
        }
        
    },
    methods: {
        login() {
            let param = {
                email: this.email,
                password:this.password
            }
            this.axios.post("http://localhost:8080/api/v1/users/login",
                JSON.stringify(param), {headers: { 'content-type': 'application/json' }}
                ).then((response) => {
                  console.log(response);
                    window.localStorage.setItem("user",
                      JSON.stringify({
                        userId: response.data,
                        email: this.email
                      })
                    )
                    window.location = "/";
                }).catch(e => {
                    console.log(e)
                    alert("이메일과 비밀번호를 확인하세요")
                })
        }
    }
}
</script>

<style>
body {
  height: 100%;
}

.form-signin {
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .checkbox {
  font-weight: 400;
}
.form-signin .form-control {
  position: relative;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

</style>