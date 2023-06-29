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
                }).catch(error =>
					Swal.fire({
						icon: 'error',
						title: 'Oh!',
						text: 'Your email or your password are incorrect'
					})
				);
            }
        },

        register() {
            if (this.registerFirstName && this.registerLastName && this.registerEmail && this.registerPassword) {
                axios.post("/api/clients", `firstName=${this.registerFirstName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}`,
                    { headers: { "content-type": "application/x-www-form-urlencoded" } })

                        axios.post("/api/login", `email=${this.registerEmail}&password=${this.registerPassword}`, { headers: { "content-type": "application/x-www-form-urlencoded" } })
                        .then((response) => {
                            console.log("login")
                            window.location.href = "/web/pages/accounts.html"
                    }).catch(error =>
                        Swal.fire({
                            icon: 'error',
                            text: error.response.data,
                            confirmButtonColor: '#F2070793',
                        })
                    );
            }
        }
        
    }
    }).mount("#app")