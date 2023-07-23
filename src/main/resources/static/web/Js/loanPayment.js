const { createApp } = Vue;
createApp({
    data() {
        return {
            accountPay: '',
            accounts: [],
            account:[],
            isActive:[],
            getId: "", 
            loan: {},
            idLoan: null, 
            errMsg: ''
        }
    },
    created() {
        this.loanInfo()
        this.activeAccounts()
    },
    methods: {
        loanRequest() {
            Swal.fire({
                title: 'Do you want to make a payment for this loan?',
                showCancelButton: true,
                confirmButtonText: 'Confirm payment',  
            }).then((res) => {
                if (res.value) {
                    axios.post(`/api/clientLoan/payments?loanPay=${this.idLoan}&account=${this.accountPay}`)
                        .then(res => {
                            Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: `Payment correct`,
                                showConfirmButton: false,
                                timer: 1500
                            })
                            setTimeout(() => {
                                window.location.href = '/web/pages/accounts.html'
                            }, 1900)
                        }).catch(err => {
                            this.errMsg = err.response.data
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: `${this.errMsg}`,
                                showConfirmButton: false,
                                timer: 1500
                            })
                        })
                }
            })
        },
        activeAccounts() {
            axios.get(`/api/clients/current/accounts`)
                .then(response => {
                    this.isActive = response.data;
                    this.isActive.sort((a,b)=> a.id - b.id)
                }).catch(err => console.log(err))
        },
      
        loanInfo() {
            this.  getId = new URLSearchParams(location.search).get('id')
            axios.get(`/api/clientLoans/${this.getId}`)
                .then(res => {
                    this.loan = res.data
                    this.idLoan = res.data.id
                    console.log(this.loan)
                }).catch(err => {
                    console.log(err)
                })
        },
        paymentsCalcu(amount, payments) {
            let division = amount / payments
            let formatDivision = parseFloat(division.toFixed(2))
            return formatDivision.toLocaleString()
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
    }
}).mount("#app")