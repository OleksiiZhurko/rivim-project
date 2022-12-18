import React, {Component} from 'react'
import StorageService from '../../../service/storageService'
import {Link} from 'react-router-dom'

class AdminHeader extends Component {
    render() {
        return (
            <nav className='navbar navbar-expand-lg navbar-light bg-light sticky-top justify-content-end'>
                <span className='navbar-brand'>
                    {StorageService.firstName + ' ' + StorageService.lastName}
                    (<Link to={'/login'} onClick={() => StorageService.clear()} className='link'>Logout</Link>)
                </span>
            </nav>
        )
    }
}

export default AdminHeader
