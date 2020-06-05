export default function checkAuthAdmin() {
  return new Promise((resolve, reject) => {
    // you can call api or do what you want in here, for this case I use localStorage
    const user = localStorage.getItem('ulogovan');
    if (user) {
        let role = localStorage.getItem('role');
        if (role === "ROLE_ADMIN")
            resolve(true);
        else {
            reject(new Error('/oglasi'));
        }
    } else {
        // If failure you can redirect url you want.
        reject(new Error('/oglasi'));
    }
  });
}
