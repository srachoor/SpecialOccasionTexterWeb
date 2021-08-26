CREATE TABLE IF NOT EXISTS user_profile (
    id UUID PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS user_account (
    id UUID PRIMARY KEY REFERENCES user_profile(id),
    username TEXT UNIQUE NOT NULL,
    user_password TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS friend (
    id UUID PRIMARY KEY,
    user_account_id UUID REFERENCES user_account(id) NOT NULL,
    friend_first_name TEXT NOT NULL,
    friend_last_name TEXT NOT NULL,
    friend_dob DATE
);

CREATE TABLE IF NOT EXISTS occasion (
    id UUID PRIMARY KEY,
    occasion_name TEXT NOT NULL,
    occasion_date TEXT NOT NULL,
    friend_id UUID REFERENCES friend(id)
);

CREATE TABLE IF NOT EXISTS reminder_text (
    id UUID PRIMARY KEY,
    text_content TEXT NOT NULL,
    text_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    phone_number TEXT NOT NULL,
    text_on BOOLEAN,
    text_sent BOOLEAN,
    occasion_id UUID REFERENCES occasion(id)
);

CREATE TABLE IF NOT EXISTS friend_text (
    id UUID PRIMARY KEY,
    text_content TEXT NOT NULL,
    text_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    phone_number TEXT NOT NULL,
    text_on BOOLEAN,
    text_sent BOOLEAN,
    occasion_id UUID REFERENCES occasion(id)
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";