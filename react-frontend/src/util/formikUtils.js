class FormikUtils {

    initUser() {
        return {
            username: '',
            password: '',
            rePassword: '',
            email: '',
            firstName: '',
            lastName: '',
            birthday: '',
            role: 'user',
        }
    }

    formUser(values) {
        return {
            username: values.username,
            password: values.password,
            email: values.email,
            firstName: values.firstName,
            lastName: values.lastName,
            birthday: values.birthday,
            role: values.role,
        }
    }
}

export default new FormikUtils()
