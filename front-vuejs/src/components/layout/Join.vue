<template>
    <div class="text-center container">
        <form class="form-signin">
            <img class="mb-4" src="../../assets/logo.png" alt="" width="72" height="72">
            <h1 class="h3 mb-3 font-weight-normal">JOIN US</h1>
            
            <label for="email" class="sr-only">Email address</label>
            <input v-model="email" type="email" id="email" name="email" class="form-control" placeholder="Email address" required>
            
            <label for="password" class="sr-only">Password</label>
            <input v-model="password" type="password" id="password" class="form-control" placeholder="Password" required>
            
            <label for="name" class="sr-only">Name</label>
            <input v-model="name" type="text" id="name" class="form-control" placeholder="Name" required>
            
            <label for="age" class="sr-only">Age</label>
            <input v-model="age" type="number" id="age" class="form-control" placeholder="Age">
            
            <select class="custom-select" v-bind="hobby">
                <option selected>HOBBY</option>
                <option value="READING">READING</option>
                <option value="SPORTS">SPORTS</option>
                <option value="WALK">WALK</option>
            </select>
            
            <button class="btn btn-lg btn-primary btn-block" type="button" @click="join()">Join</button>
        </form>
    </div>
</template>

<script>
export default {
    name: "Login",
    data() {
        return {
            email: "",
            password: "",
            name:"",
            age: null,
            hobby: null,
        }
        
    },
    methods: {
        join() {
            let params = {
                email: this.email,
                password:this.password,
                name: this.name,
                age: this.age === '' ? null : this.age,
                hobby: this.hobby
            }

            this.axios.post("http://localhost:8080/api/v1/users"
                            , JSON.stringify(params), {headers: { 'content-type': 'application/json' }})
                    .then(() => {
                      alert("회원가입이 완료되었습니다.");
                      window.location.reload();
                      this.$router.push("/posts");
                    })
                    .catch((e) => {
                      console.log(e);
                      alert("이메일과 비밀번호를 확인해주세요");
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