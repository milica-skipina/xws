import Codebook from "views/Codebook.jsx";
import Pricelist from "views/Pricelist.jsx";
import Ad from "views/Ad.jsx";
import Ads from "views/Ads.jsx";
import ShowAd from "views/ShowAd.jsx";
import Register from "views/Register.jsx";
import Requests from "views/Requests.jsx";

var routes = [
    {
        path: "/codebook",
        name: "Codebook",
        icon: "nc-icon nc-bank",
        component: Codebook,
        layout: "/admin"
      },
      {
        path: "/pricelist",
        name: "Pricelist",
        icon: "nc-icon nc-tile-56",
        component: Pricelist,
        layout: "/admin"
      },
       {
        path: "/ads",
        name: "Ads",
        icon: "nc-icon nc-caps-small",
        component: Ads,
        
      },      
      {
        path: "/newad",
        name: "New Ad",
        icon: "nc-icon nc-caps-small",
        component: Ad,
        
      },
      {
        icon: "nc-icon nc-box-2",
        path: '/requests',
        name: "Requests",    
        component: Requests,
        
      },
     
     
  
];
export default routes;
