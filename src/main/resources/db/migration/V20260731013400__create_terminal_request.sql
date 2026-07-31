CREATE TABLE IF NOT EXISTS terminal_request (
    id uuid PRIMARY KEY,
    status VARCHAR(255),
    customer_id VARCHAR(255),
    terminal_type VARCHAR(255),
    street VARCHAR(255),
    number VARCHAR(50),
    city VARCHAR(255),
    state VARCHAR(2),
    zip_code VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Add comments
COMMENT ON TABLE terminal_request IS 'Stores terminal request information for terminal requests';

COMMENT ON COLUMN terminal_request.id IS 'Unique identifier';
COMMENT ON COLUMN terminal_request.status IS 'Terminal request status';
COMMENT ON COLUMN terminal_request.customer_id IS 'Customer identifier';
COMMENT ON COLUMN terminal_request.terminal_type IS 'Terminal type (e.g., POS_WIFI)';

COMMENT ON COLUMN terminal_request.street IS 'Address street';
COMMENT ON COLUMN terminal_request.number IS 'Address number';
COMMENT ON COLUMN terminal_request.city IS 'Address city';
COMMENT ON COLUMN terminal_request.state IS 'Address state (UF)';
COMMENT ON COLUMN terminal_request.zip_code IS 'Address ZIP code';

COMMENT ON COLUMN terminal_request.created_at IS 'Creation timestamp';
COMMENT ON COLUMN terminal_request.updated_at IS 'Last update timestamp';