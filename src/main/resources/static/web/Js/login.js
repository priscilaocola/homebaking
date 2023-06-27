const { createApp } = Vue

createApp({
    data() {
        return {
            registerFirstName: "",
            registerLastName: "",
            registerEmail: "",
            registerPassword: "",
            email: "",
            password: "",
        }
    },
    created() { },
    methods: {
        login() {
            if(this.email && this.password ){
                axios.post("/api/login", `email=${this.email}&password=${this.password}`, { headers: { "content-type": "application/x-www-form-urlencoded" } })
                .then((res) => {
                    console.log("login")
                    window.location.href = "/web/pages/accounts.html"
                }).catch(err =>console.log(err))
            }else{
            alert("Complete all Fields")
            }    
        },
        register() {
            if (this.registerFirstName && this.registerLastName && this.registerEmail && this.registerPassword) {
                axios.post("/api/clients", `firstName=${this.registerFirstName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}`,
                    { headers: { "content-type": "application/x-www-form-urlencoded" } })
                    .then((res) => {
                        axios.post("/api/login", `email=${this.registerEmail}&password=${this.registerPassword}`, { headers: { "content-type": "application/x-www-form-urlencoded" } })
                        .then((res) => {
                            console.log("login")
                            window.location.href = "/web/pages/accounts.html"
                        }).catch(err =>console.log(err))
                    }).catch(error => console.log(error));
            } else {
                alert("Complete all Fields")
            }
        }
    }
    }).mount("#app")