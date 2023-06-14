const { createApp } = Vue;

createApp({
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
      axios.get(`http://localhost:8080/api/clients`)
        .then((response) => {
          this.clients = response.data;
          console.log(this.clients);
        }).catch(err => console.log(err))
      
    },
    addClient() {
      this.postClients();
    },
    postClients() {
      axios.post(`http://localhost:8080/rest/clients`, this.obj)
        .then(() => {
          this.loadData();
        })
    },

  },
}).mount("#app");
