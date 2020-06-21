import Codebook from "views/Codebook.jsx";
import Pricelist from "views/Pricelist.jsx";
import Ad from "views/Ad.jsx";
import Ads from "views/Ads.jsx";
import Profile from "views/Profile.jsx";
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
  {
    icon: "nc-icon nc-box-2",
    path: '/messages',
    name: "Messages",
    component: Messages,

  }


];
export default routes;
