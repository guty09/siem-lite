import {
    createContext,
    useContext,
    useState,
} from "react";

import type { ReactNode } from "react";

import {
    login,
    logout,
    saveToken,
    isAuthenticated,
} from "../services/auth";

import type { LoginRequest } from "../services/auth";

interface AuthContextType {
    authenticated: boolean;
    loginUser: (request: LoginRequest) => Promise<void>;
    logoutUser: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(
    undefined
);

export const AuthProvider = ({
                                 children,
                             }: {
    children: ReactNode;
}) => {
    const [authenticated, setAuthenticated] = useState(
        isAuthenticated()
    );

    const loginUser = async (request: LoginRequest) => {
        const response = await login(request);

        saveToken(response.token);

        setAuthenticated(true);
    };

    const logoutUser = () => {
        logout();

        setAuthenticated(false);
    };

    return (
        <AuthContext.Provider
            value={{
                authenticated,
                loginUser,
                logoutUser,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error(
            "useAuth must be used inside an AuthProvider"
        );
    }

    return context;
};