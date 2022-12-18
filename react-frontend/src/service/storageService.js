const KEY = 'credentials'

class StorageService {

    save(credentials) {
        localStorage.setItem(KEY, JSON.stringify(credentials))
    }

    clear() {
        localStorage.clear()
    }

    isAuthenticated() {
        return localStorage.getItem(KEY) !== null
    }

    isUserInRole(role) {
        return this.data?.role === role
    }

    get token() {
        return this.data?.token
    }

    get username() {
        return this.data?.username
    }

    get firstName() {
        return this.data?.firstName
    }

    get lastName() {
        return this.data?.lastName
    }

    get data() {
        return JSON.parse(localStorage.getItem(KEY))
    }
}

export default new StorageService()
