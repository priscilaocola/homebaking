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
        login()  {
            if (this.email && this.password) {
                if (this.email == "admin@gmail.com") {
                    axios.post("/api/login", `email=${this.email}&password=${this.password}`,
                        { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(res => {
                            console.log(res)
                            if (res.status == 200) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                                setTimeout(() => {
                                    window.location.href = "/web/pages/manager.html";
                                }, 1800)
                            }
                        }).catch(err => { console.error(err) })
                } else {
                    axios.post("/api/login", `email=${this.email}&password=${this.password}`,
                        { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(res => {
                            console.log(res)
                            if (res.status == 200) {
                                Swal.fire({
                                    title: 'Hi Welcome',
                                    imageUrl: '../asset/welcome.png',
                                    imageWidth: 400,
                                    imageHeight: 300,
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                                setTimeout(() => {
                                    window.location.href = "/web/pages/accounts.html";
                                }, 1800)
                            }
                        }).catch(err => { console.error(err) })
                }
            }
        },

        register() {
            if (this.registerFirstName && this.registerLastName && this.registerEmail && this.registerPassword) {
                axios.post("/api/clients", `firstName=${this.registerFirstName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}`,
                    { headers: { "content-type": "application/x-www-form-urlencoded" } })

                        axios.post("/api/login", `email=${this.registerEmail}&password=${this.registerPassword}`, { headers: { "content-type": "application/x-www-form-urlencoded" } })
                        .then((res) => {
                            if (res.status == 200) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                                setTimeout(() => {
                                    window.location.href = "/web/pages/accounts.html";
                                }, 1800)
                            }
                    }).catch((error) => Swal.fire({
                        icon: 'error',
                        text: 'Something went wrong!',
                    }));
            }
        }
        
    }
    }).mount("#app")