import { lazy } from 'react-router-guard';

const NewAd = lazy(() => import('./views/Pages/NewAd.js'));
const Oglasi =lazy(() => import('./views/Pages/Oglasi.js'));
const Oglas = lazy(() => import('./views/Pages/Oglas.js'));
const NoviCjenovnik = lazy(() => import('./views/Pages/NoviCjenovnik.js'));
const NewCodebook = lazy(() => import('./views/Pages/NewCodebook.js'));
const Basket = lazy(() => import('./views/Pages/Basket'));
const Requests = lazy(() => import('./views/Pages/Requests'));
const Orders =lazy(() => import('./views/Pages/Orders.js'));
const Comments = lazy(() => import('./views/Pages/Comments'));
const Profile = lazy(() => import('./views/Pages/Profile'))
const Messages = lazy(() => import('./views/Pages/Messages'))


// https://github.com/ReactTraining/react-router/tree/master/packages/react-router-config

export default  [
  { authorize: ["ROLE_SELLER"], path: '/newAd', component: NewAd },
  { authorize:["gfdsdfgh"], path: '/oglasi', component: Oglasi },
  { authorize: ["ROLE_CUSTOMER"], path: '/oglas/:id', component: Oglas },
  { authorize: ["ROLE_ADMIN"], path: '/cjenovnik', component: NoviCjenovnik },
  { authorize: ["ROLE_ADMIN"], path: '/comments', component: Comments },
  { authorize: ["ROLE_ADMIN"], path: '/codebook', component: NewCodebook },
  { authorize: ["ROLE_CUSTOMER"], path: '/basket', component: Basket },
  { authorize: ["ROLE_SELLER"], path: '/requests', component: Requests },
  { authorize: ["ROLE_SELLER"], path: '/orders', component: Orders },
  { authorize: ["ROLE_SELLER", "ROLE_ADMIN", "ROLE_CUSTOMER"], path: '/profile', component: Profile },
  { authorize: ["ROLE_SELLER", "ROLE_CUSTOMER"], path: '/messages', component: Messages },
  //{ path: '/register', component: Register },
  //{ path: '/home', component: lazy(() => import('./containers/DefaultLayout')) },

];
