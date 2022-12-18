import React, {Component, useEffect} from 'react'
import StorageService from '../../service/storageService'
import AdminService from '../../service/adminService'
import AdminHeader from './part/AdminHeader'
import {Formik} from 'formik'
import Validator from '../../util/validator'
import {Link} from 'react-router-dom'
import FormikUtils from '../../util/formikUtils'

class CreateUpdatePage extends Component {

    constructor(props) {
        super(props)

        if (!StorageService.isUserInRole('admin')) {
            this.props.history.push('/redirect')
        }

        this.state = {
            username: this.props.match.params.username,
            addMode: !this.props.match.params.username
        }
    }

    handleCreate = (values, {setErrors}) => {
        AdminService.register(FormikUtils.formUser(values))
            .then(() => {
                this.props.history.push('/admin')
            }).catch(err => {
                if (err.response.data) {
                    if (err.response.status === 401 || err.response.status === 403) {
                        StorageService.clear()
                        this.props.history.push('/login')
                    } else if (err.response.data.error === 'UsernameNotUniqueException') {
                        setErrors({username: 'Username already exists'})
                    } else if (err.response.data.error === 'EmailNotUniqueException') {
                        setErrors({email: 'Email already exists'})
                    }
                }
            })
    }

    handleUpdate = (values, {setErrors}) => {
        if (StorageService.username === values.username && values.role === 'user') {
            setErrors({role: 'I can\'t change role for myself'})
        } else {
            AdminService.update(FormikUtils.formUser(values))
                .then(() => {
                    this.props.history.push('/admin')
                }).catch(err => {
                    if (err.response?.data) {
                        if (err.response.data.error === 'EmailNotUniqueException') {
                            setErrors({email: 'Email already exists'})
                        } else if (err.response.status === 401 || err.response.status === 403) {
                            StorageService.clear()
                            this.props.history.push('/login')
                        }
                    }
                })
        }
    }

    title() {
        return this.state.addMode ? <h2>Add user</h2> : <h2>Update user</h2>
    }

    render() {
        return (
            <div>
                <AdminHeader />
                <div className='container mt-3'>
                    {this.title()}
                    <Formik
                        initialValues={FormikUtils.initUser()}
                        validate={this.state.addMode ? Validator.validateCreate : Validator.validateUpdate}
                        validationSchema={this.validationSchema}
                        onSubmit={this.state.addMode ? this.handleCreate : this.handleUpdate}>
                        {({
                              errors,
                              touched,
                              values,
                              handleBlur,
                              handleChange,
                              setFieldValue,
                              handleSubmit
                          }) => {
                            if (!this.state.addMode) {
                                useEffect(() => {
                                    AdminService.getUser(this.state.username).then(result => {
                                        setFieldValue('username', result.data['username'], false)
                                        setFieldValue('password', result.data['password'], false)
                                        setFieldValue('rePassword', '', false)
                                        setFieldValue('email', result.data['email'], false)
                                        setFieldValue('firstName', result.data['firstName'], false)
                                        setFieldValue('lastName', result.data['lastName'], false)
                                        setFieldValue('birthday', new Date(result.data['birthday']).toISOString().substring(0, 10), false)
                                        setFieldValue('role', result.data['role'], false)
                                    }).catch(errors => {
                                        if (errors) {
                                            this.props.history.push('/admin')
                                        }
                                    })
                                }, [setFieldValue])
                            }

                            return (
                                <form onSubmit={handleSubmit}>
                                    {errors.non_field_errors && <div>{errors.non_field_errors}</div>}
                                    <div className={'form-group row' + (this.state.addMode ? ' required' : '')}>
                                        <label htmlFor='inputUsername' className='col-sm-2 col-form-label'>
                                            Username
                                        </label>
                                        <div className='col-sm-10'>
                                            <input type='text'
                                                   className={'form-control' + (touched.username && errors.username ? ' is-invalid' : '')}
                                                   name='username'
                                                   id='inputUsername'
                                                   readOnly={!this.state.addMode}
                                                   onChange={handleChange}
                                                   onBlur={handleBlur}
                                                   value={values.username}/>
                                            {touched.username && errors.username && <div className={'error'}>{errors.username}</div>}
                                        </div>
                                    </div>
                                    <div className={'form-group row' + (this.state.addMode ? ' required' : '')}>
                                        <label htmlFor='inputPassword' className='col-sm-2 col-form-label'>
                                            Password
                                        </label>
                                        <div className='col-sm-10'>
                                            <input type='password'
                                                   className={'form-control' + (touched.password && errors.password ? ' is-invalid' : '')}
                                                   name='password'
                                                   id='inputPassword'
                                                   onChange={handleChange}
                                                   onBlur={handleBlur}
                                                   value={values.password}/>
                                            {touched.password && errors.password && <div className={'error'}>{errors.password}</div>}
                                        </div>
                                    </div>
                                    <div className={'form-group row' + (this.state.addMode ? ' required' : '')}>
                                        <label htmlFor='inputPasswordAgain' className='col-sm-2 col-form-label'>
                                            Password confirm
                                        </label>
                                        <div className='col-sm-10'>
                                            <input type='password'
                                                   className={'form-control' + (touched.rePassword && errors.rePassword ? ' is-invalid' : '')}
                                                   name='rePassword'
                                                   id='inputPasswordAgain'
                                                   onChange={handleChange}
                                                   onBlur={handleBlur}
                                                   value={values.rePassword}/>
                                            {touched.rePassword && errors.rePassword && <div className={'error'}>{errors.rePassword}</div>}
                                        </div>
                                    </div>
                                    <div className='form-group required row'>
                                        <label htmlFor='inputEmail' className='col-sm-2 col-form-label'>
                                            Email
                                        </label>
                                        <div className='col-sm-10'>
                                            <input type='email'
                                                   className={'form-control' + (touched.email && errors.email ? ' is-invalid' : '')}
                                                   name='email'
                                                   id='inputEmail'
                                                   onChange={handleChange}
                                                   onBlur={handleBlur}
                                                   value={values.email}/>
                                            {touched.email && errors.email && <div className={'error'}>{errors.email}</div>}
                                        </div>
                                    </div>
                                    <div className='form-group required row'>
                                        <label htmlFor='inputFirstName' className='col-sm-2 col-form-label'>
                                            First name
                                        </label>
                                        <div className='col-sm-10'>
                                            <input type='text'
                                                   className={'form-control' + (touched.firstName && errors.firstName ? ' is-invalid' : '')}
                                                   name='firstName'
                                                   id='inputFirstName'
                                                   onChange={handleChange}
                                                   onBlur={handleBlur}
                                                   value={values.firstName}/>
                                            {touched.firstName && errors.firstName && <div className={'error'}>{errors.firstName}</div>}
                                        </div>
                                    </div>
                                    <div className='form-group required row'>
                                        <label htmlFor='inputLastName' className='col-sm-2 col-form-label'>
                                            Last name
                                        </label>
                                        <div className='col-sm-10'>
                                            <input type='text'
                                                   className={'form-control' + (touched.lastName && errors.lastName ? ' is-invalid' : '')}
                                                   name='lastName'
                                                   id='inputLastName'
                                                   onChange={handleChange}
                                                   onBlur={handleBlur}
                                                   value={values.lastName}/>
                                            {touched.lastName && errors.lastName && <div className={'error'}>{errors.lastName}</div>}
                                        </div>
                                    </div>
                                    <div className='form-group required row'>
                                        <label htmlFor='inputBirthday' className='col-sm-2 col-form-label'>
                                            Birthday
                                        </label>
                                        <div className='col-sm-10'>
                                            <input type='date'
                                                   className={'form-control' + (touched.birthday && errors.birthday ? ' is-invalid' : '')}
                                                   name='birthday'
                                                   id='inputBirthday'
                                                   onChange={handleChange}
                                                   onBlur={handleBlur}
                                                   value={values.birthday}/>
                                            {touched.birthday && errors.birthday && <div className={'error'}>{errors.birthday}</div>}
                                        </div>
                                    </div>
                                    <div className='form-group required row'>
                                        <label className='col-sm-2 col-form-label' htmlFor='inputRole'>
                                            Role
                                        </label>
                                        <div className='col-sm-10'>
                                            <select name='role'
                                                    id='inputRole'
                                                    className={'custom-select form-control' + (touched.role && errors.role ? ' is-invalid' : '')}
                                                    onChange={handleChange}
                                                    onBlur={handleBlur}
                                                    value={values.role}>
                                                <option value='user' label='User' />
                                                <option value='admin' label='Admin' />
                                            </select>
                                            {touched.role && errors.role && <div className={'error'}>{errors.role}</div>}
                                        </div>
                                    </div>
                                    <div className='form-group row'>
                                        <div className='col-sm-10'>
                                            <button type='submit' className='btn btn-dark mr-3'>Ok</button>
                                            <Link to={'/admin'} className='btn btn-light'>Cancel</Link>
                                        </div>
                                    </div>
                                </form>
                            )}}
                    </Formik>
                </div>
            </div>
        )
    }
}

export default CreateUpdatePage
