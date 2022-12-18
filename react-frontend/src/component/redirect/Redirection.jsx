import React, {Component} from 'react'
import StorageService from '../../service/storageService'

class Redirection extends Component {

    constructor(props) {
        super(props)

        if (StorageService.isAuthenticated()) {
            if (StorageService.isUserInRole('user')) {
                this.props.history.push('/user')
            } else if (StorageService.isUserInRole('admin')) {
                this.props.history.push('/admin')
            } else {
                StorageService.clear()
                this.props.history.push('/login')
            }
        } else {
            this.props.history.push('/login')
        }
    }

    render() {
        return (<></>)
    }
}

export default Redirection
