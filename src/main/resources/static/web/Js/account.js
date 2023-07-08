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
      axios.get(`/api/accounts/${this.getId}`)
        .then((res) => {
          const accounts = res.data;
          this.allAccounts = accounts.transactions
          console.log(this.allAccounts);
          
          this.allAccounts.sort((a, b) => b.id - a.id);

        }) .catch((error) => console.log(error));
    },
    logout() {
      Swal.fire({
  title: 'Bye see you soon',
  imageUrl: '../asset/BYE BYE.png',
  imageWidth: 400,
  imageHeight: 250,
  imageAlt: 'Custom image',
  preConfirm: login => {
    return axios
      .post('/api/logout')
      .then(response => {
        window.location.href = '/web/pages/index.html';
      })
      .catch(error => {
        Swal.showValidationMessage(`Request failed: ${error}`);
      })
      
    },
  })
    }
    },
  
}).mount("#app");