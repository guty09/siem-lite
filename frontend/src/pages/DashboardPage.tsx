import { Button, Container, Paper, Typography } from "@mui/material";

import { useAuth } from "../context/AuthContext";

export default function DashboardPage() {
    const { logoutUser } = useAuth();

    const handleLogout = () => {
        logoutUser();
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 4 }}>
            <Paper sx={{ p: 4 }}>
                <Typography variant="h3" gutterBottom>
                    SIEM-Lite Dashboard
                </Typography>

                <Typography variant="body1" sx={{ mb: 3 }}>
                    Welcome! Authentication is working.
                </Typography>

                <Button
                    variant="contained"
                    color="error"
                    onClick={handleLogout}
                >
                    Logout
                </Button>
            </Paper>
        </Container>
    );
}