const SESSION_KEY = "loggedInUser";

/** Returns true if a user is currently logged in */
export function isLoggedIn() {
  return !!localStorage.getItem(SESSION_KEY);
}

/** Returns the logged-in username, or null */
export function getUser() {
  return localStorage.getItem(SESSION_KEY) || null;
}

/** Log in a user by username */
export function login(username) {
  localStorage.setItem(SESSION_KEY, username);
  // Notify other components on the same tab (Navbar ↔ RegisterComplaint)
  window.dispatchEvent(new Event("authChange"));
}

/** Log out the current user */
export function logout() {
  localStorage.removeItem(SESSION_KEY);
  window.dispatchEvent(new Event("authChange"));
}