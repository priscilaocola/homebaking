
const { createApp } = Vue;

createApp({
  data() {
    return {
      account: [],
      getId: "",
      transactions: [], 
      dateStart: "",
      dateEnd: "",
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
          this.account = accounts.transactions;
          console.log(this.account);
          this.account.sort((a, b) => b.id - a.id);
          this.dateStart = this.account[this.account.length - 1].date.split('T')[0]; 
          this.dateEnd = this.account[0].date.split('T')[0]; 
		}).catch((error) => console.log(error));
    },
    logout() {
      Swal.fire({
        title: 'Bye see you soon',
        imageUrl: '../asset/BYE.png',
        imageWidth: 400,
        imageHeight: 300,
        preConfirm: login => {
          return axios
            .post('/api/logout')
            .then(response => {
              window.location.href = '/web/pages/index.html';
            })
            .catch(error => {
              Swal.showValidationMessage(`Request failed: ${error}`);
            });
        },
      });
    },
    pdfDow() {
      Swal.fire({
        title: 'Are you sure you want to download these transfers?',
        inputAttributes: {
          autocapitalize: 'off',
        },
        showCancelButton: true,
        confirmButtonText: 'CONFIRME',
        showLoaderOnConfirm: true,
        preConfirm: pdf => {
          return axios.post(`/api/accounts/transactions/pdf?id=${this.getId}&startDate=${this.dateStart}&endDate=${this.dateEnd}`)
            .then(response => {
              Swal.fire({
                icon: 'success',
                text: 'Downloaded successfully',
              });
            })
            .catch(error => {
              Swal.fire({
                icon: 'error',
                text: error.response.data,
                confirmButtonColor: '#7c601893',
              });
            });
        },
    
      });
    },
	
  },

}).mount("#app");