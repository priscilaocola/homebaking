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
      // json: "",
      loanName: '',
      loanMaxAmount: null,
      loanPayments: [],
      loanInterest: null,
      loan: {
        name: '',
        maxAmount: null,
        payments: [],
        percentage: null
      },
      inputNumber: null

    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios.get(`/api/clients`)
        .then((response) => {
          this.clients = response.data;
          console.log(this.clients);
        }).catch(err => console.log(err))

    },
    addClient() {
      if (this.obj.firstName !== "" && this.obj.lastName !== "" && this.obj.email !== "") {
        this.postClient();
      } else {
        alert("All fields are necessary");
      }

    },
    postClients() {

      axios.post(`/rest/clients?firstName${this.obj.firstName}&lastName${this.obj.lastName}&email${this.obj.email}`)
        .then(res => {
          this.loadData()
          this.clearAllFields()
        }).catch(err => console.log(err))
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
    clearAllFields() {
      this.obj.firstName = '',
        this.obj.lastName = '',
        this.obj.email = ''
    },
    deleteClient(id) {
      axios.delete(id)
        .then(res => {
          this.loadData()
        }).catch(err => console.log(err))
    },
    createLoan() {
      axios.post(`/api/loans/create`, this.loan)
        .then(response => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Loan Created',
            showConfirmButton: false,
            timer: 1500
          })
          this.clearForm()
        })
        .catch(error => {
          Swal.fire({
            icon: 'error',
            text: error.response.data,
            confirmButtonColor: '#7c601893',
          });
        });
    },
    addNumber() {
      if (this.inputNumber !== null) {
        this.loan.payments.push(this.inputNumber)
        this.inputNumber = null
      }
    },
    clearPayments() {
      this.loan.payments = []
    },
    clearForm() {
      this.loan.name = ''
      this.loan.maxAmount = null
      this.loan.payments = []
      this.loan.interest = null
    },

  },
}).mount("#app");
