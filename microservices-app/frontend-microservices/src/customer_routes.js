
import Ads from "views/Ads.jsx";
import Orders from "views/Orders.jsx";
import Profile from "views/Profile.jsx";
import Ad from "views/Ad.jsx";
import Requests from "views/Requests.jsx";
import Messages from "views/Messages.jsx";

var routes = [
  {
    path: "/profile",
    name: "Profile",
    icon: "fa fa-user-circle",
    component: Profile,
  },
  {
    path: "/ads",
    name: "Ads",
    icon: "nc-icon nc-caps-small",
    component: Ads,

  },
   {
        path: "/orders",
        name: "Orders",
        icon: "nc-icon nc-spaceship",
        component: Orders,
        
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
  {
    icon: "nc-icon nc-box-2",
    path: '/messages',
    name: "Messages",
    component: Messages,
  }
];
export default routes;
