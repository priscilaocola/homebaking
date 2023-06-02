const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            obj: {
                firstName: "",
                lastName: "",
                email: "",
            },
            clients: [],
            json: "",
        };
    },
    created() {
        this.loadData();
    },

    methods: {
        loadData() {
            axios
                .get("http://localhost:8080/clients")
                .then((response) => {
                    this.json = response.data;
                    this.clients = response.data._embedded.clients;
                    console.log(this.clients);
                    this.restResponse = response;
                })
                .catch((error) => console.log(error));
        },
        addClient() {
            this.postClients();
        },
        postClients() {
            axios.post("http://localhost:8080/clients", this.obj).then(() => {
                this.loadData();
            });
        },
    },
});
app.mount("#app");
