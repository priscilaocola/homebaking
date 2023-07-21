const { createApp } = Vue;

createApp({
    data() {
        return {
            registerFirstName: "",
            registerLastName: "",
            registerEmail: "",
            registerPassword: "",
            email: "",
            password: "",
        };
    },
    created() { },
    methods: {
        login() {
            axios
                .post("/api/login", `email=${this.email}&password=${this.password}`, {
                    headers: { "content-type": "application/x-www-form-urlencoded" },
                })
                .then((response) => {
                    Swal.fire({
                        title: "Hi Welcome",
                        imageUrl: "../asset/welcome.png",
                        imageWidth: 400,
                        imageHeight: 300,
                        showConfirmButton: false,
                        timer: 1500,
                    }).then(() => {
                        if (this.email == "admin@gmail.com") {
                            window.location.href = "/web/pages/manager.html";
                        } else {
                            window.location.href = "/web/pages/accounts.html";
                        }
                    });
                })
                .catch((error) => {
                    console.error(error);
                });
        },

        register() {
            axios.post(
                "/api/clients",
                `firstName=${this.registerFirstName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}`,
                { headers: { "content-type": "application/x-www-form-urlencoded" } }
            );
            axios
                .post(
                    "/api/login",
                    `email=${this.registerEmail}&password=${this.registerPassword}`,
                    { headers: { "content-type": "application/x-www-form-urlencoded" } }
                )
                .then((response) => {
                    Swal.fire({
                        title: "Registration with success",
                        imageUrl: "../asset/welcome.png",
                        imageWidth: 400,
                        imageHeight: 300,
                        showConfirmButton: false,
                        timer: 1500,
                    });
                    setTimeout(() => {
                        window.location.href = "/web/pages/accounts.html";
                    }, 1800);
                })
                .catch((error) =>
                    Swal.fire({
                        icon: "error",
                        text: "Sorry something went wrong!",
                    })
                );
        },
    },
}).mount("#app");
