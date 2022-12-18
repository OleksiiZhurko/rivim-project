import axios from 'axios'
import StorageService from './storageService'

const authUrl = process.env.REACT_APP_SERVER_URL + '/auth/'

class AuthService {

    login(credentials) {
        return axios.post(authUrl + 'login', credentials).then(response => {
            StorageService.save(response.data)
        })
    }

    register(user) {
        return axios.post(authUrl + 'register', user)
    }

    getCaptcha() {
        return axios.get(authUrl + 'generateCaptcha')
    }
}

export default new AuthService()
