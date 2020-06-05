
let renderAdmin = localStorage.getItem("ulogovan") === "ROLE_ADMIN" ? false : true ;
let renderCustomer = localStorage.getItem("ulogovan") === "ROLE_CUSTOMER" ? false : true ;
let renderSeller = localStorage.getItem("ulogovan") === "ROLE_SELLER" ? false : true ;

console.log("mojeee")
console.log(localStorage.getItem("ulogovan"))


export default {
  items: [
    {
      
      name: 'New ad',
      url: '/newAd',
      icon: 'icon-drop',
    },
    {

      name: 'Ads',
      url: '/oglasi',
      icon: 'icon-drop',
    },
    {
      
      name: 'Pricelist',
      url: '/cjenovnik',
      icon: 'icon-drop',
    },
    {
      
      name: 'Codebook',
      url: '/codebook',
      icon: 'icon-drop',
    },



  ],
};
