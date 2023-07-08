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
                  
                }).catch((error) => 
                Swal.fire({
                icon: 'error',
                text: 'Something went wrong!',
            }));
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