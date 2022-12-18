import React, {Component} from 'react'
import StorageService from '../../service/storageService'

class UserPage extends Component {

    constructor(props) {
        super(props)

        if (!StorageService.isUserInRole('user')) {
            this.props.history.push('/redirect')
        }
    }

    logout() {
        StorageService.clear()
        this.props.history.push('/login')
    }

    render() {
        return (
            <div className='container mt-5'>
                <h1 className='text-center'>Hello, {StorageService.firstName}</h1>
                <p className='text-center'>Click <span onClick={() => this.logout()} className='link'>here</span> to logout</p>
            </div>
        )
    }
}

export default UserPage
