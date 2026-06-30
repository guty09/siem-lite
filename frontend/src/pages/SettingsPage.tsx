import { Typography } from "@mui/material";
import AppLayout from "../components/layout/AppLayout";

export default function SettingsPage() {
    return (
        <AppLayout title="Settings">
            <Typography sx={{ color: "#9ca3af" }}>
                Application settings.
            </Typography>
        </AppLayout>
    );
}