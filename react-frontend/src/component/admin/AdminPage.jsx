import React, {Component} from 'react'
import StorageService from '../../service/storageService'
import AdminService from '../../service/adminService'
import AdminHeader from './part/AdminHeader'
import {Link} from 'react-router-dom'
import {ToastContainer} from 'react-toastify'
import ToastTemplate from '../../util/toastTemplate'


class AdminPage extends Component {

    constructor(props) {
        super(props)

        this.state = {
            users: []
        }

        if (!StorageService.isUserInRole('admin')) {
            this.props.history.push('/redirect')
        } else {
            this.initUsers()
        }
    }

    initUsers() {
        AdminService.getAllUsers()
            .then((result) => {
                this.setState({users: result.data})
            }).catch(err => {
                if (err.response?.status === 401 || err.response?.status === 403) {
                    ToastTemplate.warn('Session expired')
                    StorageService.clear()
                    this.props.history.push('/login')
                } else if (err.response?.data?.reasons) {
                    ToastTemplate.warn(err.response.data.reasons)
                } else {
                    ToastTemplate.error('Internal server error')
                }
            })
    }

    deleteUser(username) {
        if (StorageService.username !== username) {
            // eslint-disable-next-line no-restricted-globals
            if (confirm('Are you sure want to delete?')) {
                AdminService.deleteUser(username)
                    .then(() => {
                        this.initUsers()
                        ToastTemplate.success(username + ' was deleted')
                    }).catch(err => {
                        if (err.response?.data?.reasons) {
                            ToastTemplate.warn(err.response.data.reasons)
                        } else {
                            ToastTemplate.error('Internal server error')
                        }
                    })
            }
        } else {
            ToastTemplate.warn('I can\'t delete myself')
        }
    }

    render() {
        return (
            <div>
                <ToastContainer />
                <div>
                    <AdminHeader />
                    <div className='container mt-3'>
                        <Link to={'/admin/create_user'} className='link'>Add new user</Link>
                        <table className='table my-3'>
                            <thead>
                            <tr>
                                <th scope='col'>Login</th>
                                <th scope='col'>First Name</th>
                                <th scope='col'>Last Name</th>
                                <th scope='col'>Age</th>
                                <th scope='col'>Role</th>
                                <th scope='col'>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                this.state.users.map(
                                    (user, index) =>
                                        <tr key={index}>
                                            <td>{user.username}</td>
                                            <td>{user.firstName}</td>
                                            <td>{user.lastName}</td>
                                            <td>{user.age}</td>
                                            <td>{user.role}</td>
                                            <td>
                                                <Link to={'/admin/update_user/' + user.username} className='mr-2 link'>Edit</Link>
                                                <span onClick={() => this.deleteUser(user.username)} className='link'>Delete</span>
                                            </td>
                                        </tr>
                                )
                            }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        )
    }
}

export default AdminPage
