import { Typography } from "@mui/material";
import AppLayout from "../components/layout/AppLayout";

export default function AuditLogsPage() {
    return (
        <AppLayout title="Audit Logs">
            <Typography sx={{ color: "#9ca3af" }}>
                Audit activity will appear here.
            </Typography>
        </AppLayout>
    );
}