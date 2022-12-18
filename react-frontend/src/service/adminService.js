import axios from 'axios'
import StorageService from './storageService'

const adminUrl = process.env.REACT_APP_SERVER_URL + '/admin/'

class AdminService {

    get headers() {
        return {
            headers: {
                Authorization: StorageService.token
            }
        }
    }

    getAllUsers() {
        return axios.get(adminUrl + 'get_all_users', this.headers)
    }

    getUser(username) {
        return axios.get(adminUrl + 'get_user/' + username, this.headers)
    }

    register(user) {
        return axios.post(adminUrl + 'add_user', user, this.headers)
    }

    update(user) {
        return axios.put(adminUrl + 'update_user', user, this.headers)
    }

    deleteUser(username) {
        return axios.delete(adminUrl + 'delete_user/' + username, this.headers)
    }
}

export default new AdminService()
