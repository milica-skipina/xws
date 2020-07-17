/*!

=========================================================
* Paper Dashboard React - v1.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/paper-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Licensed under MIT (https://github.com/creativetimofficial/paper-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import Dashboard from "views/Dashboard.jsx";
import Codebook from "views/Codebook.jsx";
import Pricelist from "views/Pricelist.jsx"
import CommentRequests from "views/CommentRequests";
import Profile from "views/Profile";
import Messages from "views/Messages.jsx";

var routes = [
    {
        // path: "/profile/" + localStorage.getItem('name'),
        path: "/profile",
        name: "Profile",
        icon: "fa fa-user-circle",
        component: Profile,
        layout: "/admin"
    },

    {
        path: "/codebook",
        name: "Codebook",
        icon: "fa fa-book",
        component: Codebook,
        layout: "/admin"
    },


    {
        path: "/dashboard",
        name: "Dashboard",
        icon: "nc-icon nc-bank",
        component: Dashboard,

    },

    {
        path: "/comments",
        name: "Comments",
        icon: "fa fa-comments-o",
        component: CommentRequests,

    }

];
export default routes;
