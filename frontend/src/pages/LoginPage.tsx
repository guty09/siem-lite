import { useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    Alert,
    Box,
    Button,
    Container,
    Paper,
    TextField,
    Typography,
} from "@mui/material";

import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
    const navigate = useNavigate();

    const { loginUser } = useAuth();

    const [username, setUsername] = useState("");

    const [password, setPassword] = useState("");

    const [error, setError] = useState("");

    const handleLogin = async () => {
        try {
            setError("");

            await loginUser({
                username,
                password,
            });

            navigate("/dashboard");
        } catch {
            setError("Invalid username or password.");
        }
    };

    return (
        <Container maxWidth="sm">
            <Box
                sx={{
                    display: "flex",
                    height: "100vh",
                    alignItems: "center",
                    justifyContent: "center",
                }}
            >
                <Paper
                    elevation={6}
                    sx={{
                        p: 4,
                        width: "100%",
                    }}
                >
                    <Typography
                        variant="h4"
                        align="center"
                        gutterBottom
                    >
                        SIEM-Lite
                    </Typography>

                    <Typography
                        variant="body2"
                        align="center"
                        sx={{ mb: 3 }}
                    >
                        Security Operations Dashboard
                    </Typography>

                    {error && (
                        <Alert severity="error" sx={{ mb: 2 }}>
                            {error}
                        </Alert>
                    )}

                    <TextField
                        label="Username"
                        fullWidth
                        margin="normal"
                        value={username}
                        onChange={(e) =>
                            setUsername(e.target.value)
                        }
                    />

                    <TextField
                        label="Password"
                        type="password"
                        fullWidth
                        margin="normal"
                        value={password}
                        onChange={(e) =>
                            setPassword(e.target.value)
                        }
                    />

                    <Button
                        variant="contained"
                        fullWidth
                        sx={{ mt: 3 }}
                        onClick={handleLogin}
                    >
                        Sign In
                    </Button>
                </Paper>
            </Box>
        </Container>
    );
}