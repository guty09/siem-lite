import type { ReactNode } from "react";
import {
    AppBar,
    Box,
    Button,
    CssBaseline,
    Divider,
    Drawer,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
} from "@mui/material";

import DashboardIcon from "@mui/icons-material/Dashboard";
import WarningAmberIcon from "@mui/icons-material/WarningAmber";
import StorageIcon from "@mui/icons-material/Storage";
import SecurityIcon from "@mui/icons-material/Security";
import ReportProblemIcon from "@mui/icons-material/ReportProblem";
import HistoryIcon from "@mui/icons-material/History";
import SettingsIcon from "@mui/icons-material/Settings";
import HomeIcon from "@mui/icons-material/Home";
import LogoutIcon from "@mui/icons-material/Logout";

import { Link, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const drawerWidth = 260;

interface Props {
    children: ReactNode;
    title: string;
}

export default function AppLayout({ children, title }: Props) {
    const { logoutUser } = useAuth();
    const location = useLocation();

    return (
        <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "#0f172a" }}>
            <CssBaseline />

            <AppBar
                position="fixed"
                sx={{
                    zIndex: 1201,
                    bgcolor: "#111827",
                    borderBottom: "1px solid #1f2937",
                }}
                elevation={0}
            >
                <Toolbar>
                    <Typography
                        variant="h6"
                        sx={{ flexGrow: 1, fontWeight: 700 }}
                    >
                        SIEM-Lite SOC Console
                    </Typography>

                    <Button
                        color="inherit"
                        startIcon={<HomeIcon />}
                        component={Link}
                        to="/dashboard"
                        sx={{ mr: 2 }}
                    >
                        Home
                    </Button>

                    <Button
                        color="inherit"
                        startIcon={<LogoutIcon />}
                        onClick={logoutUser}
                    >
                        Logout
                    </Button>
                </Toolbar>
            </AppBar>

            <Drawer
                variant="permanent"
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    "& .MuiDrawer-paper": {
                        width: drawerWidth,
                        bgcolor: "#111827",
                        color: "#e5e7eb",
                        borderRight: "1px solid #1f2937",
                        boxSizing: "border-box",
                    },
                }}
            >
                <Toolbar />

                <Box sx={{ p: 2 }}>
                    <Typography variant="overline">
                        Navigation
                    </Typography>
                </Box>

                <List>
                    <ListItemButton
                        component={Link}
                        to="/dashboard"
                        selected={location.pathname === "/dashboard"}
                    >
                        <ListItemIcon sx={{ color: "#93c5fd" }}>
                            <DashboardIcon />
                        </ListItemIcon>
                        <ListItemText primary="Dashboard" />
                    </ListItemButton>

                    <ListItemButton
                        component={Link}
                        to="/alerts"
                        selected={location.pathname === "/alerts"}
                    >
                        <ListItemIcon sx={{ color: "#fca5a5" }}>
                            <WarningAmberIcon />
                        </ListItemIcon>
                        <ListItemText primary="Alerts" />
                    </ListItemButton>

                    <ListItemButton
                        component={Link}
                        to="/events"
                        selected={location.pathname === "/events"}
                    >
                        <ListItemIcon sx={{ color: "#86efac" }}>
                            <StorageIcon />
                        </ListItemIcon>
                        <ListItemText primary="Events" />
                    </ListItemButton>

                    <ListItemButton
                        component={Link}
                        to="/iocs"
                        selected={location.pathname === "/iocs"}
                    >
                        <ListItemIcon sx={{ color: "#fbbf24" }}>
                            <SecurityIcon />
                        </ListItemIcon>
                        <ListItemText primary="Threat Intelligence" />
                    </ListItemButton>

                    <ListItemButton
                        component={Link}
                        to="/incidents"
                        selected={location.pathname === "/incidents"}
                    >
                        <ListItemIcon sx={{ color: "#fb7185" }}>
                            <ReportProblemIcon />
                        </ListItemIcon>
                        <ListItemText primary="Incidents" />
                    </ListItemButton>

                    <ListItemButton
                        component={Link}
                        to="/audit"
                        selected={location.pathname === "/audit"}
                    >
                        <ListItemIcon sx={{ color: "#c4b5fd" }}>
                            <HistoryIcon />
                        </ListItemIcon>
                        <ListItemText primary="Audit Logs" />
                    </ListItemButton>

                    <ListItemButton
                        component={Link}
                        to="/settings"
                        selected={location.pathname === "/settings"}
                    >
                        <ListItemIcon sx={{ color: "#fde68a" }}>
                            <SettingsIcon />
                        </ListItemIcon>
                        <ListItemText primary="Settings" />
                    </ListItemButton>
                </List>

                <Divider sx={{ borderColor: "#1f2937", mt: 2 }} />
            </Drawer>

            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 4,
                    color: "#e5e7eb",
                }}
            >
                <Toolbar />

                <Typography
                    variant="h4"
                    sx={{
                        fontWeight: 800,
                        mb: 4,
                    }}
                >
                    {title}
                </Typography>

                {children}
            </Box>
        </Box>
    );
}