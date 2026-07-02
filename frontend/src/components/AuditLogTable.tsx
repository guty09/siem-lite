import {
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Chip,
} from "@mui/material";

import type { AuditLog } from "../types/audit";

interface AuditLogTableProps {
    logs: AuditLog[];
}

function getChipColor(action: string) {
    if (action.startsWith("USER")) {
        return "primary";
    }

    if (action.startsWith("ALERT")) {
        return "warning";
    }

    return "default";
}

export default function AuditLogTable({ logs }: AuditLogTableProps) {
    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Timestamp</TableCell>
                        <TableCell>Action</TableCell>
                        <TableCell>Username</TableCell>
                        <TableCell>Details</TableCell>
                    </TableRow>
                </TableHead>

                <TableBody>
                    {logs.map((log) => (
                        <TableRow key={log.id} hover>
                            <TableCell>
                                {new Date(log.timestamp).toLocaleString()}
                            </TableCell>

                            <TableCell>
                                <Chip
                                    label={log.action}
                                    color={getChipColor(log.action)}
                                    size="small"
                                />
                            </TableCell>

                            <TableCell>{log.username}</TableCell>

                            <TableCell>{log.details}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}