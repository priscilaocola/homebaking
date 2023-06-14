const { createApp } = Vue;

createApp({
  data() {
    return {
      allAccounts: [],
      getId: "",
      transactions: [],
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      
      this.getId = new URLSearchParams(location.search).get("id"); 
      axios.get(`http://localhost:8080/api/accounts/${this.getId}`)
        .then((res) => {
          const accounts = res.data;
          this.allAccounts = accounts.transactions
          console.log(this.allAccounts);

          this.allAccounts.sort((a, b) => b.id - a.id);
            
        }) .catch((err) => console.log(err));
    },
  },
}).mount("#app");